package com.atixlabs.semillasmiddleware.app.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
    private int code = HttpStatus.OK.value();

    @Builder.Default
    private String result = Result.SUCCESS.description;
    private String body;
    private String userMessage;

    public static ApiResponse badRequest(){
        return ApiResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .result(Result.ERROR.description)
                .build();
    }

    public static ApiResponse internalError(){
        return ApiResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
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
