package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoan;
import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BondareaService {


    private LoanRepository loanRepository;

    @Autowired
    public BondareaService (LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }
    @Value("${bondarea.base_url}")
    private String serviceURL;

    @Value("${bondarea.access_key}")
    private String access_key;

    @Value("${bondarea.access_token}")
    private String access_token;

    @Value("${bondarea.idm}")
    private String idm;

    // ID de la cuenta representado
    //private String idAccount;

    // Valores separados por "|" (pipe) de las columnas a mostrar para cada registro. (Ej. sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m)
    private String loanColumns;

    // Valores separados por "|" (pipe) de los estados de préstamos a listar.  (Ej. 55|60)
   // private String loanstate;





    //contains json converter and date format configuration
    private Gson gson = null;

    //contains service link with factory
    private Retrofit retrofit = null;

    private BondareaEndpoint bondareaEndpoint = null;



    public void initializeBondareaApi() {
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

        bondareaEndpoint = retrofit.create(BondareaEndpoint.class);


        log.info("initialize bondarea api:");

    }

    public List<BondareaLoan> getLoans(String idAccount, String loanState) throws Exception {

        initializeBondareaApi();
        log.info("getLoans:");

        //Fill url params
        loanColumns = "sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m";


        Call<BondareaLoanResponse> callSync = bondareaEndpoint.getLoans("comunidad","wspre", "prerepprestamos", access_key, access_token, idm, /*idAccount,*/ loanColumns, loanState);
        log.info("url request " + callSync.request().url());

        BondareaLoanResponse bondareaLoanResponse = null;
        try {
            Response<BondareaLoanResponse> response = callSync.execute();
            log.info("el response envio ... " + response.raw().toString());
            log.info("Response getLoans status : " + response.code() + " " + response.message());

            if (response.code() == 200) {
                bondareaLoanResponse = response.body();
                log.info("RESPONSE:");
                log.info("Response body " + response.body());
                log.info("  " + bondareaLoanResponse.toString());

            }
            else
            {
                return Collections.emptyList();
            }
        }catch(JsonSyntaxException  ex){
            log.error(" Bondarea retrieved object does not match with model ", ex);
            throw new Exception("retrived object does not match with model ");
        }
        catch (SocketTimeoutException ex){
                log.error(" Bondarea timeout ", ex);
             throw new Exception("Tiemout, please try again");
            }

        catch(Exception ex){
            log.error("Bondarea error ", ex);
            throw new Exception("Error, please try again");
        }

        return bondareaLoanResponse.getLoans();

    }

    //Second endpoint


}
