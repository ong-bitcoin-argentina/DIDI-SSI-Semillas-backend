package com.atixlabs.semillasmiddleware.app.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse {
    enum Result {
        SUCCESS("success"),
        ERROR("error");

        private String description;

        Result (String description){
            this.description = description;
        }

    }

    @Builder.Default
    private String result = Result.SUCCESS.description;
    private String body;
    private String userMessage;

    public static ApiResponse error(){
        return ApiResponse.builder()
                .result(Result.ERROR.description)
                .build();
    }

    public ApiResponse setBody(String body){
        this.body = body;
        return this;
    }

    public ApiResponse setUserMessage(String message){
        this.userMessage = message;
        return this;
    }

}
