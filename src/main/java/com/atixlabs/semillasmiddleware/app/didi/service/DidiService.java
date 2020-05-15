package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.app.didi.dto.*;
import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.repository.DidiAppUserRepository;
import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
//import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DidiService {

    private DidiEndpoint endpointInterface = null; //BindEndpointInterface bindEndpointInterface = null;

    private CredentialRepository credentialRepository;
    private CredentialStateRepository credentialStateRepository;
    private DidiAppUserRepository didiAppUserRepository;
    private CredentialIdentityRepository credentialIdentityRepository;
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;
    private CredentialDwellingRepository credentialDwellingRepository;
    private CredentialBenefitsRepository credentialBenefitsRepository;
    private CredentialCreditRepository credentialCreditRepository;

    @Autowired
    public DidiService(
            CredentialRepository credentialRepository,
            CredentialStateRepository credentialStateRepository,
            DidiAppUserRepository didiAppUserRepository,

            CredentialIdentityRepository credentialIdentityRepository,
            CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository,
            CredentialDwellingRepository credentialDwellingRepository,
            CredentialBenefitsRepository credentialBenefitsRepository,
            CredentialCreditRepository credentialCreditRepository
    ) {
        this.credentialRepository = credentialRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.didiAppUserRepository = didiAppUserRepository;

        this.credentialIdentityRepository = credentialIdentityRepository;
        this.credentialEntrepreneurshipRepository = credentialEntrepreneurshipRepository;
        this.credentialDwellingRepository = credentialDwellingRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.credentialCreditRepository = credentialCreditRepository;

        this.endpointInterface = (DidiEndpoint) endpointInterfaceBuilder(DidiEndpoint.class);
    }

    private Object endpointInterfaceBuilder(Class<?> classToCreateEndpoint) {
        log.info("initializeServiceController:");
        String serviceURL = "http://192.81.218.211:3500/api/1.0/didi_issuer/";

        //contains json converter and date format configuration
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        //ScalarsConverterFactory - allows String response for debug purposes.
        //GsonConverterFactory - decodes response into final target object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(classToCreateEndpoint);
    }


    public String getAuthToken() {
        log.info("getAuthToken:");
        DidiAuthRequestBody didiAuthRequestBody = new DidiAuthRequestBody("semillas", "semillas_password");
        Call<DidiAuthResponse> callSync = endpointInterface.getAuthToken(didiAuthRequestBody);

        try {
            Response<DidiAuthResponse> response = callSync.execute();
            DidiAuthResponse didiAuthResponse = response.body();

            if (didiAuthResponse != null)
                return didiAuthResponse.getData().getToken();
        } catch (Exception ex) {
            log.error("getAuthTokenSync: Didi Authorization error", ex);
        }
        return null;
    }

    public String didiCredentialSync(){
        log.info("didiCredentialSync:");

        //1-Busco registros en AppUser con estado SYNC_MISSING O SYNC_ERROR (todo: validar si SYNC_ERROR vuelvo a intentar):
        ArrayList<String> didiSyncStatus = new ArrayList<>();
        didiSyncStatus.add(DidiSyncStatus.SYNC_MISSING.getCode());
        didiSyncStatus.add(DidiSyncStatus.SYNC_ERROR.getCode());
        ArrayList<DidiAppUser> didiAppUsers = didiAppUserRepository.findBySyncStatusIn(didiSyncStatus);

        if (didiAppUsers.size()<=0)
            return "Ningun usuario que sincronizar con didi";

        ArrayList<Long> dniList = new ArrayList<>();
        for (DidiAppUser didiAppUser : didiAppUsers) {
            dniList.add(didiAppUser.getDni());
        }

        //2-Busco credenciales que tienen el listado de DNIs (en estado PENDING_DIDI + OK? - REVOCADAS)
        ArrayList<Credential> pendingCredentials = getCredentialsByCredentialStateAndBeneficiaryDniIn(
                CredentialStatesCodes.PENDING_DIDI.getCode(),
                dniList
        );

        if (pendingCredentials == null || pendingCredentials.size()<=0)
            return "Los Did guardados no tienen su credencial creada anteriormente mediante la encuesta";

        log.info("El usuario fue encontrado en la base y tiene una credencial de identidad en estado PENDING_DIDI");
        //3-TODO: LLAMAR METODO DE REVOCACION DE URIEL (validar que sucede si revoco pero luego didi falla)

        //4-Invoco a Didi para cada credencial en la lista (todo: validar si tengo que revocar las activas en didi)
        for (Credential pendingCredential : pendingCredentials) {
            log.info(pendingCredential.toString());

            String currentDid = Objects.requireNonNull(findAppUserFromDni(pendingCredential.getBeneficiaryDni(), didiAppUsers)).getDid();

            DidiCredentialData didiCredentialData = new DidiCredentialData(pendingCredential, currentDid);
            DidiCreateCredentialResponse didiCreateCredentialResponse = this.createCertificateDidi(didiCredentialData);

            if (didiCreateCredentialResponse!= null && didiCreateCredentialResponse.getStatus().equals("success")) {
                log.info("la credencial fue creada con exito persistiendo datos en la base");

                String credentialDidiId = didiCreateCredentialResponse.getData().get(0).get_id();
                //String newDid = didiCreateCredentialResponse.getData().get(0).getData().getDidFromParticipant();

                //todo: ACA ESTOY HACIENDO UN UPDATE QUE SOLO ESTA BIEN SI ES PRE-CREDENCIAL
                if (pendingCredential.getCredentialState().getStateName().equals(CredentialStatesCodes.PENDING_DIDI.getCode())) {
                    //actualizo cuando es una pre-credencial en estado PENDING_DIDI sino doy de alta una nueva.
                    pendingCredential.setIdDidiReceptor(currentDid);
                    pendingCredential.setIdDidiCredential(credentialDidiId);
                    credentialRepository.save(pendingCredential);
                }
                else {
                    //es una credencial con estado activo o revocado, debo crear una nueva.
                    //todo: validar si tomo el valor de tipo de credencial y creo una nueva del mismo tipo
                    switch (CredentialCategoriesCodes.getEnumByStringValue(pendingCredential.getCredentialCategory())){
                        case IDENTITY:
                            Optional<CredentialIdentity> credentialIdentityOp = credentialIdentityRepository.findById(pendingCredential.getId());
                            CredentialIdentity credentialIdentity = new CredentialIdentity(credentialIdentityOp.get());
                            credentialIdentity.setIdDidiReceptor(currentDid);
                            credentialIdentity.setIdDidiCredential(credentialDidiId);
                            setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialIdentity);
                            credentialIdentityRepository.save(credentialIdentity);
                            break;
                        case DWELLING:
                            Optional<CredentialDwelling> credentialDwellingOp = credentialDwellingRepository.findById(pendingCredential.getId());
                            CredentialDwelling credentialDwelling = new CredentialDwelling(credentialDwellingOp.get());
                            credentialDwelling.setIdDidiReceptor(currentDid);
                            credentialDwelling.setIdDidiCredential(credentialDidiId);
                            setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialDwelling);
                            credentialDwellingRepository.save(credentialDwelling);
                            break;
                        case ENTREPRENEURSHIP:
                            Optional<CredentialEntrepreneurship> credentialEntrepreneurshipOp = credentialEntrepreneurshipRepository.findById(pendingCredential.getId());
                            CredentialEntrepreneurship credentialEntrepreneurship = new CredentialEntrepreneurship(credentialEntrepreneurshipOp.get());
                            credentialEntrepreneurship.setIdDidiReceptor(currentDid);
                            credentialEntrepreneurship.setIdDidiCredential(credentialDidiId);
                            setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialEntrepreneurship);
                            credentialEntrepreneurshipRepository.save(credentialEntrepreneurship);
                            break;
                        case BENEFIT:
                            Optional<CredentialBenefits> credentialBenefitsOp = credentialBenefitsRepository.findById(pendingCredential.getId());
                            CredentialBenefits credentialBenefits = new CredentialBenefits(credentialBenefitsOp.get());
                            credentialBenefits.setIdDidiReceptor(currentDid);
                            credentialBenefits.setIdDidiCredential(credentialDidiId);
                            setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialBenefits);
                            credentialBenefitsRepository.save(credentialBenefits);
                            break;
                        case CREDIT:
                            Optional<CredentialCredit> credentialCreditOp = credentialCreditRepository.findById(pendingCredential.getId());
                            CredentialCredit credentialCredit = new CredentialCredit(credentialCreditOp.get());
                            credentialCredit.setIdDidiReceptor(currentDid);
                            credentialCredit.setIdDidiCredential(credentialDidiId);
                            setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialCredit);
                            credentialCreditRepository.save(credentialCredit);
                            break;
                        default:
                            log.error("El tipo de credencial indicado no existe");
                    }
                }

                DidiAppUser didiAppUser = findAppUserFromDni(pendingCredential.getBeneficiaryDni(), didiAppUsers);
                if (didiAppUser!=null) {
                    didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_OK.getCode());
                    didiAppUserRepository.save(didiAppUser);
                }
                log.info("La credencial fue actualizada con exito, se obtuvo el id de didi: " + credentialDidiId);
            }
            else
                log.error("Ocurrio un error al intentar crear la credencial en didi");
        }

        return "finalizado el proceso de sync";
    }

    private ArrayList<Credential> getCredentialsByCredentialStateAndBeneficiaryDniIn(String credentialState, ArrayList<Long> dniList){
        Optional<CredentialState> credentialStatePending = credentialStateRepository.findByStateName(credentialState);
        if (credentialStatePending.isPresent())
            return credentialRepository.findByCredentialStateAndBeneficiaryDniIn(credentialStatePending.get(), dniList);
        return null;
    }

    private Credential setCredentialState(String credentialStateString, Credential credential){
        Optional<CredentialState> credentialState = credentialStateRepository.findByStateName(credentialStateString);
        if (credentialState.isPresent()){
            credential.setCredentialState(credentialState.get());
            return credential;
        }
        return null;
    }

    private DidiAppUser findAppUserFromDni(Long dni, ArrayList<DidiAppUser> didiAppUsers) {
        //busco el nuevo did asociado al dni
        for (DidiAppUser didiAppUser : didiAppUsers) {
            if (didiAppUser.getDni().equals(dni))
                return didiAppUser;
        }
        return null;
    }

    private DidiCreateCredentialResponse createCertificateDidi(DidiCredentialData didiCredentialData){
        String token = getAuthToken();

        Call<DidiCreateCredentialResponse> callSync = endpointInterface.createCertificate(
                token,
                "5eb589be3ac4af0256d2053a",
                true,
                didiCredentialData
        );

        log.info(didiCredentialData.toString());
        try {
            Response<DidiCreateCredentialResponse> response = callSync.execute();
            log.info("createCertificate - response:");
            if (response.body() != null)
                log.info(response.body().toString());
            return response.body();
        }
        catch (Exception ex) {
            log.error("createCertificateDidi: Didi Request error", ex);
        }

        return null;
    }

    private DidiCreateCredentialResponse emmitCertificateDidi(String didiCredentialId){
        String token = getAuthToken();

        Call<DidiCreateCredentialResponse> callSync = endpointInterface.emmitCertificate(
                token,
                didiCredentialId,
                didiCredentialId
        );

        try {
            Response<DidiCreateCredentialResponse> response = callSync.execute();
            log.info("emmitCertificate: response:");
            if (response.body() != null)
                log.info(response.body().toString());
            return response.body();
        }
        catch (Exception ex) {
            log.error("emmitCertificateDidi: Didi Request error", ex);
        }

        return null;
    }

}