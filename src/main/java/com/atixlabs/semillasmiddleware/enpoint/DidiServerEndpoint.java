package com.atixlabs.semillasmiddleware.enpoint;

import com.atixlabs.semillasmiddleware.app.didi.dto.DidiUpdateIdentityResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface DidiServerEndpoint {

    @FormUrlEncoded
    @PATCH("semillas/identityValidation")
    Call<DidiUpdateIdentityResponse> updateIdentityValidationRequest(@Field("did") String did, @Field("state") String state);

}
