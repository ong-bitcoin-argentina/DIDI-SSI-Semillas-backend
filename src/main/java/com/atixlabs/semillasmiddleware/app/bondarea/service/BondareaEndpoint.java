package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BondareaEndpoint {

    @GET("{access_key}{access_token}{idm}{idCuenta}{cols}{estados}")
    Call<BondareaLoanResponse> getLoans(@Path("access_key") String access_key,
                                        @Path("access_token") String access_token,
                                        @Path("idm") String idm,
                                        @Path("idCuenta") String idCuenta,
                                        @Path("cols") String columns,
                                        @Path("estados") String states);



}
