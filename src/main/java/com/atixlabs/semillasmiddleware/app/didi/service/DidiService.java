package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAuthRequest;
import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAuthResponse;
import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DidiService {

    //contains json converter and date format configuration
    private Gson gson = null;

    //contains service link with factory
    private Retrofit retrofit = null;

    private DidiEndpoint endpointInterface = null; //BindEndpointInterface bindEndpointInterface = null;

    private DidiAuthRequest didiAuthRequest = null;
    private String token;



    private CredentialRepository credentialRepository;
    private CredentialCreditRepository credentialCreditRepository;
    private CredentialIdentityRepository credentialIdentityRepository;
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;
    private CredentialDwellingRepository credentialDwellingRepository;
    private PersonRepository personRepository;
    private LoanRepository loanRepository;
    private CredentialBenefitsRepository credentialBenefitsRepository;
    private DIDHistoricRepository didHistoricRepository;
    private CredentialStateRepository credentialStateRepository;
    private ParameterConfigurationRepository parameterConfigurationRepository;
    private AnswerCategoryFactory answerCategoryFactory;




    @Autowired
    public DidiService(
            CredentialCreditRepository credentialCreditRepository,
            CredentialRepository credentialRepository,
            PersonRepository personRepository,
            LoanRepository loanRepository,
            CredentialBenefitsRepository credentialBenefitsRepository,
            DIDHistoricRepository didHistoricRepository,
            CredentialStateRepository credentialStateRepository,
            AnswerCategoryFactory answerCategoryFactory,
            CredentialIdentityRepository credentialIdentityRepository,
            CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository,
            CredentialDwellingRepository credentialDwellingRepository,
            ParameterConfigurationRepository parameterConfigurationRepository
    ) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.didHistoricRepository = didHistoricRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
        this.answerCategoryFactory = answerCategoryFactory;
        this.credentialIdentityRepository = credentialIdentityRepository;
        this.credentialEntrepreneurshipRepository = credentialEntrepreneurshipRepository;
        this.credentialDwellingRepository = credentialDwellingRepository;
    }





    public ArrayList<Credential> createCredentialDidi(){

        Optional<CredentialState> credentialStatePending = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());

        ArrayList<Credential> pendingCredentialListOp = credentialRepository.findByCredentialCategoryAndCredentialState(
                CredentialCategoriesCodes.IDENTITY.getCode(),
                credentialStatePending.get()
        );

        if (!pendingCredentialListOp.isEmpty()){
            for (Credential credential : pendingCredentialListOp) {
                log.info(credential.toString());
            }
        }


        return pendingCredentialListOp;

    }

    public void initializeServiceController() {

        String serviceURL = "http://192.81.218.211:3500/api/1.0/didi_issuer/";
        String username = "semillas";
        String password = "semillas_password";


        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        endpointInterface = retrofit.create(DidiEndpoint.class);
        didiAuthRequest = new DidiAuthRequest(username, password);

        log.info("initializeServiceController:");
        log.info(this.toString());
    }


    public DidiAuthResponse getAuthTokenSync() {

        initializeServiceController();

        log.info(didiAuthRequest.toString());


        Call<String> callSync = endpointInterface.getAuthToken(didiAuthRequest);

        try {

            log.info(callSync.execute().body().toString());
            /*
            Response<DidiAuthResponse> response = callSync.execute();
            DidiAuthResponse didiAuthResponse = response.body();

            token = "JWT "+didiAuthResponse.getData().getToken();
            log.info("TOKEN: " +token);

            return didiAuthResponse;
            */
        } catch (Exception ex) {
            log.error("getAuthTokenSync: Bind Authorization error", ex);
        }

        return null;
    }

}

