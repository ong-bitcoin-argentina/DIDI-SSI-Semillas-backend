package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.dto.*;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DidiService {

    private DidiEndpoint endpointInterface = null; //BindEndpointInterface bindEndpointInterface = null;

    private CredentialRepository credentialRepository;
    private CredentialStateRepository credentialStateRepository;

    @Autowired
    public DidiService(
            CredentialRepository credentialRepository,
            CredentialStateRepository credentialStateRepository
    ) {
        this.credentialRepository = credentialRepository;
        this.credentialStateRepository = credentialStateRepository;

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

        //contains service link with factory
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

    public DidiCreateCredentialResponse createCredentialDidi(DidiAppUser didiAppUser){
        log.info("createCredentialDidi:");

        Optional<Credential> pendingCredentialOp = getPendingCredentials(didiAppUser.getDni());

        if (pendingCredentialOp.isPresent()){
            log.info("Aca tengo que actualizar la data del user");
            return  createCredentialDidiCall(new DidiCredentialData(pendingCredentialOp.get(), didiAppUser.getDid()));
        }

        return null;
    }

    private Optional<Credential> getPendingCredentials(Long beneficiaryDni){
        Optional<CredentialState> credentialStatePending = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());

        if (credentialStatePending.isPresent()){
            ArrayList<CredentialState> credentialStates = new ArrayList<>();
            credentialStates.add(credentialStatePending.get());
            return credentialRepository.findByBeneficiaryDniAndAndCredentialCategoryAndCredentialStateIn(beneficiaryDni, CredentialCategoriesCodes.IDENTITY.getCode(), credentialStates);
        }

        return Optional.empty();
    }

    private DidiCreateCredentialResponse createCredentialDidiCall(DidiCredentialData didiCredentialData){
        String token = getAuthToken();

        Call<DidiCreateCredentialResponse> callSync = endpointInterface.createCredential(
                token,
                "5eb589be3ac4af0256d2053a",
                true,
                didiCredentialData
        );

        log.info(didiCredentialData.toString());
        try {
            Response<DidiCreateCredentialResponse> response = callSync.execute();
            log.info("didi-response-message:");
            if (response.body() != null)
                log.info(response.body().toString());
            return response.body();
        }
        catch (Exception ex) {
            log.error("createCredentialDidiCall: Didi Request error", ex);
        }

        return null;
    }

}

/*
        if (pendingCredentialOp != null) {
            for (Credential credential : pendingCredentialOp) {
                didiResponse.add(createCredentialDidiCall(new DidiCredentialRespData(credential)));
            }
        }

*/
