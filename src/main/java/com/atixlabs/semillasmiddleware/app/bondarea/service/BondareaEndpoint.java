package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BondareaEndpoint {

    @GET("/")
    Call<BondareaLoanResponse> getLoans( @Query(value = "c") String c,
                                         @Query(value = "v"  ) String v,
                                        @Query(value = "url" ) String url,
                                        @Query(value = "access_key") String access_key,
                                        @Query(value = "access_token") String access_token,
                                        @Query(value = "idm") String idm,
                                      //  @Query("idCuenta") String idCuenta,
                                        @Query(value = "cols") String columns,
                                        @Query(value = "estados") String states,
                                        @Query(value = "idPrestamo") String idLoan,
                                        @Query(value = "fechaCalculo") String date);
    }
