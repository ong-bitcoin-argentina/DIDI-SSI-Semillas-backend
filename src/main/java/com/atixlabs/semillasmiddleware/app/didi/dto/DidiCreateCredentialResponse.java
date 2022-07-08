package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DidiCreateCredentialResponse {
    private String status;

    private Object data;

    private String code;//when error
    private String message;//when error

}