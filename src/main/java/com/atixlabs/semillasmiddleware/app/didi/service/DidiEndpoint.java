package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface DidiEndpoint {

    @POST("user/login")
    Call<DidiAuthResponse> getAuthToken(@Body DidiAuthRequestBody didiAuthRequestBody);

    @POST("Cert/")
    @FormUrlEncoded
    Call<DidiCreateCredentialResponse> createCredential(
            @Header("token") String token,
            @Field("templateId") String templateId,
            @Field("split") boolean split,
            @Field("data") DidiCredentialData didiCredentialData);

/*
    @POST("banks/{bank_id}/accounts/{account_id}/{view_id}/wallet/cvu")
    Call<BindCVUResponse> createCVU(@Header("Authorization") String token,
                                    @Path("bank_id") Integer bank_id,
                                    @Path("account_id") String account_id,
                                    @Path("view_id") String view_id,
                                    @Body BindCVURequestBody bindCVURequestBody);
*/

}
