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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
//import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DidiService {

    private DidiEndpoint endpointInterface = null;
    private String didiAuthToken = null;

    private CredentialRepository credentialRepository;
    private CredentialStateRepository credentialStateRepository;
    private DidiAppUserRepository didiAppUserRepository;
    private CredentialIdentityRepository credentialIdentityRepository;
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;
    private CredentialDwellingRepository credentialDwellingRepository;
    private CredentialBenefitsRepository credentialBenefitsRepository;
    private CredentialCreditRepository credentialCreditRepository;

    private String didiBaseUrl;
    private String didiUsername;
    private String didiPassword;
    private String didiTemplateCodeIdentity;
    private String didiTemplateCodeEntrepreneurship;
    private String didiTemplateCodeDwelling;
    private String didiTemplateCodeBenefit;
/*
    @Value("${didi.base_url}")
    private String didiBaseUrl;
    @Value("${didi.username}")
    private String didiUsername;
    @Value("${didi.password}")
    private String didiPassword;
    @Value("${didi.template_code_identity}")
    private String didiTemplateCodeIdentity;
    @Value("${didi.template_code_entrepreneurship}")
    private String didiTemplateCodeEntrepreneurship;
    @Value("${didi.template_code_dwelling}")
    private String didiTemplateCodeDwelling;
    @Value("${didi.template_code_benefit}")
    private String didiTemplateCodeBenefit;
*/
    @Autowired
    public DidiService(
            CredentialRepository credentialRepository,
            CredentialStateRepository credentialStateRepository,
            DidiAppUserRepository didiAppUserRepository,

            CredentialIdentityRepository credentialIdentityRepository,
            CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository,
            CredentialDwellingRepository credentialDwellingRepository,
            CredentialBenefitsRepository credentialBenefitsRepository,
            CredentialCreditRepository credentialCreditRepository,
            @Value("${didi.base_url}") String didiBaseUrl,
            @Value("${didi.username}") String didiUsername,
            @Value("${didi.password}") String didiPassword,
            @Value("${didi.template_code_identity}") String didiTemplateCodeIdentity,
            @Value("${didi.template_code_entrepreneurship}") String didiTemplateCodeEntrepreneurship,
            @Value("${didi.template_code_dwelling}") String didiTemplateCodeDwelling,
            @Value("${didi.template_code_benefit}") String didiTemplateCodeBenefit) {
        this.credentialRepository = credentialRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.didiAppUserRepository = didiAppUserRepository;

        this.credentialIdentityRepository = credentialIdentityRepository;
        this.credentialEntrepreneurshipRepository = credentialEntrepreneurshipRepository;
        this.credentialDwellingRepository = credentialDwellingRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.credentialCreditRepository = credentialCreditRepository;
        this.didiBaseUrl = didiBaseUrl;
        this.didiUsername = didiUsername;
        this.didiPassword = didiPassword;
        this.didiTemplateCodeIdentity = didiTemplateCodeIdentity;
        this.didiTemplateCodeEntrepreneurship = didiTemplateCodeEntrepreneurship;
        this.didiTemplateCodeDwelling = didiTemplateCodeDwelling;
        this.didiTemplateCodeBenefit = didiTemplateCodeBenefit;

        this.setUpRetrofitForDidiAndGetToken();
    }

    private void setUpRetrofitForDidiAndGetToken() {
        if (this.endpointInterface == null)
            this.endpointInterface = (DidiEndpoint) endpointInterfaceBuilder(DidiEndpoint.class);
        if (didiAuthToken == null)
            this.didiAuthToken = getAuthToken();
    }

    private Object endpointInterfaceBuilder(Class<?> classToCreateEndpoint) {
        log.info("endpointInterfaceBuilder - setting up retrofit configuration:");

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
                .baseUrl(didiBaseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(classToCreateEndpoint);
    }


    public String getAuthToken() {
        log.info("getAuthToken:");
        DidiAuthRequestBody didiAuthRequestBody = new DidiAuthRequestBody(didiUsername, didiPassword);
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

    public String didiCredentialSync() {
        log.info("didiCredentialSync:");

        //1-Busco registros en AppUser con estado SYNC_MISSING O SYNC_ERROR (todo: validar si SYNC_ERROR vuelvo a intentar):
        ArrayList<String> didiSyncStatus = new ArrayList<>();
        didiSyncStatus.add(DidiSyncStatus.SYNC_MISSING.getCode());
        didiSyncStatus.add(DidiSyncStatus.SYNC_ERROR.getCode());
        ArrayList<DidiAppUser> didiAppUsers = didiAppUserRepository.findBySyncStatusIn(didiSyncStatus);

        if (didiAppUsers.size() <= 0)
            return "No existen credenciales pendientes para enviar hacia didi";

        ArrayList<Long> dniList = new ArrayList<>();
        for (DidiAppUser didiAppUser : didiAppUsers) {
            dniList.add(didiAppUser.getDni());
        }

        //2a-Busco credenciales que tengan el DNI como creditHolder - indica si es titular.
        ArrayList<Credential> creditHolders = credentialRepository.findByCreditHolderDniIn(dniList);

        //3-Creo y emito credenciales de titulares
        for (Credential credential : creditHolders) {
            //es titular de alguna credito - debo emitir en didi todas las credenciales.
            String creditHolderReceivedDid = findAppUserDidByDni(credential.getCreditHolderDni(), didiAppUsers).getDid();
            credential.setIdDidiReceptor(creditHolderReceivedDid);//registro el did recibido
            createAndEmmitCertificateDidi(credential);
            updateAppUserStatus(credential.getCreditHolderDni(), didiAppUsers);
        }


        //4-Busco credenciales que tengan el DNI como beneficiary
        ArrayList<Credential> beneficiaries = credentialRepository.findByBeneficiaryDniIn(dniList);

        //5-Invoco a Didi para cada credencial en la lista (todo: validar si tengo que revocar las activas en didi)
        for (Credential credential : beneficiaries) {
            //es beneficiario de algun credito - debo emitir solamente su credencial.
            //IMPORTANT: cuando creditHolder = beneficiary ya se cubrio en el for anterior.
            if (!credential.getCreditHolderDni().equals(credential.getBeneficiaryDni())) {
                String beneficiaryReceivedDid = findAppUserDidByDni(credential.getBeneficiaryDni(), didiAppUsers).getDid();
                credential.setIdDidiReceptor(beneficiaryReceivedDid);//registro el did recibido
                createAndEmmitCertificateDidi(credential);
                updateAppUserStatus(credential.getBeneficiaryDni(), didiAppUsers);
            }
        }

        return "finalizado el proceso de sync";
    }

    private void createAndEmmitCertificateDidi(Credential credential) {

        DidiCreateCredentialResponse didiCreateCredentialResponse = this.createCertificateDidi(credential);

        if (didiCreateCredentialResponse != null && didiCreateCredentialResponse.getStatus().equals("success")) {

            log.info("didiCertificateId to emmit: "+didiCreateCredentialResponse.getData().get(0).get_id());
            DidiEmmitCredentialResponse didiEmmitCredentialResponse = emmitCertificateDidi(didiCreateCredentialResponse.getData().get(0).get_id());

            if (didiEmmitCredentialResponse!=null)
                log.info("didiEmmitCertificate Response: "+didiEmmitCredentialResponse.toString());

            if (didiEmmitCredentialResponse!=null && didiEmmitCredentialResponse.getStatus().equals("success")){
                log.info("La credencial fue emitida en didi, persistiendo datos en bd");
                this.saveEmittedCredential(didiEmmitCredentialResponse, credential);
            }
            else {
                log.error("Fallo la emision de la credencial en didi borrando el certificado creado pero no-emitido del didi-issuer");
                //todo: eliminar la credencial creada pero no emitida.
                this.didiDeleteCertificate(didiCreateCredentialResponse.getData().get(0).get_id());
            }
        } else {
            log.error("fallo la creacion de la credencial en didi");
        }
    }



    private DidiCreateCredentialResponse createCertificateDidi(Credential credential) {
        log.info("createCertificateDidi");

        String didiTemplateCode = "";
        switch (CredentialCategoriesCodes.getEnumByStringValue(credential.getCredentialCategory())) {
            case IDENTITY:
                didiTemplateCode = didiTemplateCodeIdentity;
                break;
            case ENTREPRENEURSHIP:
                didiTemplateCode = didiTemplateCodeEntrepreneurship;
                break;
            case DWELLING:
                didiTemplateCode = didiTemplateCodeDwelling;
                break;
            case BENEFIT:
                didiTemplateCode = didiTemplateCodeBenefit;
                break;
            default:
                log.error("La categoria de credencial no es valida");
                return null;
        }


        DidiCredentialData didiCredentialData = new DidiCredentialData(credential);
        return createCertificateDidiCall(didiTemplateCode, didiCredentialData);
    }

    public DidiCreateCredentialResponse createCertificateDidiCall(String didiTemplateCode, DidiCredentialData didiCredentialData) {
        log.info("createCertificateDidiCall");

        Call<DidiCreateCredentialResponse> callSync = endpointInterface.createCertificate(didiAuthToken,didiTemplateCode,true,didiCredentialData);

        log.info(didiCredentialData.toString());
        try {
            Response<DidiCreateCredentialResponse> response = callSync.execute();
            log.info("createCertificateDidiCall - response:");
            if (response.body() != null)
                log.info("RESPONSE: " + response.body().toString());
            return response.body();
        } catch (Exception ex) {
            log.error("createCertificateDidiCall: Didi Request error", ex);
        }
        return null;
    }


    private DidiEmmitCredentialResponse emmitCertificateDidi(String didiCredentialId) {

        Call<DidiEmmitCredentialResponse> callSync = endpointInterface.emmitCertificate(
                didiAuthToken,
                didiCredentialId);

        try {
            Response<DidiEmmitCredentialResponse> response = callSync.execute();
            log.info("emmitCertificate: response:");
            if (response.body() != null)
                log.info(response.body().toString());
            return response.body();
        } catch (Exception ex) {
            log.error("emmitCertificateDidi: Didi Request error", ex);
        }

        return null;
    }

    public DidiEmmitCredentialResponse didiDeleteCertificate(String CredentialToRevokeDidiId) {

        Call<DidiEmmitCredentialResponse> callSync = endpointInterface.deleteCertificate(
                didiAuthToken,
                CredentialToRevokeDidiId);

        try {
            Response<DidiEmmitCredentialResponse> response = callSync.execute();
            log.info("deleteCertificate: response:");
            if (response.body() != null)
                log.info(response.body().toString());
            return response.body();
        } catch (Exception ex) {
            log.error("emmitCertificateDidi: Didi Request error", ex);
        }
        return null;
    }

    private void saveEmittedCredential(DidiEmmitCredentialResponse didiEmmitCredentialResponse, Credential pendingCredential) {
        log.info("saveEmittedCredential:");

        if (didiEmmitCredentialResponse != null && didiEmmitCredentialResponse.getStatus().equals("success")) {
            String credentialDidiId = didiEmmitCredentialResponse.getData().get_id();

            //todo: ACA ESTOY HACIENDO UN UPDATE QUE SOLO ESTA BIEN SI ES PRE-CREDENCIAL
            if (pendingCredential.getCredentialState().getStateName().equals(CredentialStatesCodes.PENDING_DIDI.getCode())) {
                //actualizo cuando es una pre-credencial en estado PENDING_DIDI sino doy de alta una nueva.
                pendingCredential.setIdDidiCredential(credentialDidiId);
                setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), pendingCredential);
                credentialRepository.save(pendingCredential);
            } else {
                //es una credencial con estado activo o revocado, debo crear una nueva.
                switch (CredentialCategoriesCodes.getEnumByStringValue(pendingCredential.getCredentialCategory())) {
                    case IDENTITY:
                        Optional<CredentialIdentity> credentialIdentityOp = credentialIdentityRepository.findById(pendingCredential.getId());
                        CredentialIdentity credentialIdentity = new CredentialIdentity(credentialIdentityOp.get());
                        credentialIdentity.setIdDidiCredential(credentialDidiId);
                        setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialIdentity);
                        credentialIdentityRepository.save(credentialIdentity);
                        break;
                    case DWELLING:
                        Optional<CredentialDwelling> credentialDwellingOp = credentialDwellingRepository.findById(pendingCredential.getId());
                        CredentialDwelling credentialDwelling = new CredentialDwelling(credentialDwellingOp.get());
                        credentialDwelling.setIdDidiCredential(credentialDidiId);
                        setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialDwelling);
                        credentialDwellingRepository.save(credentialDwelling);
                        break;
                    case ENTREPRENEURSHIP:
                        Optional<CredentialEntrepreneurship> credentialEntrepreneurshipOp = credentialEntrepreneurshipRepository.findById(pendingCredential.getId());
                        CredentialEntrepreneurship credentialEntrepreneurship = new CredentialEntrepreneurship(credentialEntrepreneurshipOp.get());
                        credentialEntrepreneurship.setIdDidiCredential(credentialDidiId);
                        setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialEntrepreneurship);
                        credentialEntrepreneurshipRepository.save(credentialEntrepreneurship);
                        break;
                    case BENEFIT:
                        Optional<CredentialBenefits> credentialBenefitsOp = credentialBenefitsRepository.findById(pendingCredential.getId());
                        CredentialBenefits credentialBenefits = new CredentialBenefits(credentialBenefitsOp.get());
                        credentialBenefits.setIdDidiCredential(credentialDidiId);
                        setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialBenefits);
                        credentialBenefitsRepository.save(credentialBenefits);
                        break;
                    case CREDIT:
                        Optional<CredentialCredit> credentialCreditOp = credentialCreditRepository.findById(pendingCredential.getId());
                        CredentialCredit credentialCredit = new CredentialCredit(credentialCreditOp.get());
                        credentialCredit.setIdDidiCredential(credentialDidiId);
                        setCredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), credentialCredit);
                        credentialCreditRepository.save(credentialCredit);
                        break;
                    default:
                        log.error("El tipo de credencial indicado no existe");
                }
            }

            log.info("La credencial fue actualizada con exito, se obtuvo el id de didi: " + credentialDidiId);
        } else
            log.error("Ocurrio un error al intentar crear la credencial en didi");
    }

    private void setCredentialState(String credentialStateString, Credential credential) {
        Optional<CredentialState> credentialState = credentialStateRepository.findByStateName(credentialStateString);
        credentialState.ifPresent(credential::setCredentialState);
    }

    private DidiAppUser findAppUserDidByDni(Long dni, ArrayList<DidiAppUser> didiAppUsers) {
        //busco el nuevo did asociado al dni
        for (DidiAppUser didiAppUser : didiAppUsers) {
            if (didiAppUser.getDni().equals(dni))
                return didiAppUser;
        }
        return null;
    }

    private void updateAppUserStatus(Long dni, ArrayList<DidiAppUser> didiAppUsers) {
        DidiAppUser didiAppUser = findAppUserDidByDni(dni, didiAppUsers);
        if (didiAppUser != null) {
            didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_OK.getCode());
            didiAppUserRepository.save(didiAppUser);
        }
    }

    public DidiGetAllCredentialResponse didiGetAllCredentials(){

        Call<DidiGetAllCredentialResponse> callSync = endpointInterface.getAllCertificates(didiAuthToken);

        try {
            Response<DidiGetAllCredentialResponse> response = callSync.execute();
            log.info("didiGetAllCredentials: response:");
            if (response.body() != null)
                log.info(response.body().toString());
            return response.body();
        } catch (Exception ex) {
            log.error("getAllCredentials: Didi Request error", ex);
        }
        return null;
    }

}