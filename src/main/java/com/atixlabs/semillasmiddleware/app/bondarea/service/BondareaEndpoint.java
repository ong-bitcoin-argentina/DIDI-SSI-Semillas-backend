package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BondareaEndpoint {

    @GET("/")
    Call<BondareaLoanResponse> getLoans( @Query(value = "c", encoded = true) String c,
                                         @Query(value = "v" , encoded = true ) String v,
                                        @Query(value = "url" , encoded = true) String url,
                                        @Query(value = "access_key", encoded = true) String access_key,
                                        @Query(value = "access_token", encoded = true) String access_token,
                                        @Query(value = "idm", encoded = true) String idm,
                                      //  @Query("idCuenta") String idCuenta,
                                        @Query(value = "cols", encoded = true) String columns,
                                        @Query(value = "estados", encoded = true) String states);


    // second endpoint to validate one persona and their loan status



}
