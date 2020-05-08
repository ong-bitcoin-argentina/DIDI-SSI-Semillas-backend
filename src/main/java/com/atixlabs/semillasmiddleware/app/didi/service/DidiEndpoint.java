package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAuthRequest;
import retrofit2.Call;
import retrofit2.http.*;

public interface DidiEndpoint {
/*
    @GET("/")
    Call<BondareaLoanResponse> getLoans(@Query(value = "c") String c,
                                        @Query(value = "v") String v,
                                        @Query(value = "url") String url,
                                        @Query(value = "access_key") String access_key,
                                        @Query(value = "access_token") String access_token,
                                        @Query(value = "idm") String idm,
                                        //  @Query("idCuenta") String idCuenta,
                                        @Query(value = "cols") String columns,
                                        @Query(value = "estados") String states);
*/

    // second endpoint to validate one persona and their loan status -> validar

    @POST("user/login")
    Call<String> getAuthToken(@Body DidiAuthRequest didiAuthRequest);
/*
    @POST("banks/{bank_id}/accounts/{account_id}/{view_id}/wallet/cvu")
    Call<BindCVUResponse> createCVU(@Header("Authorization") String token,
                                    @Path("bank_id") Integer bank_id,
                                    @Path("account_id") String account_id,
                                    @Path("view_id") String view_id,
                                    @Body BindCVURequestBody bindCVURequestBody);
*/

}
