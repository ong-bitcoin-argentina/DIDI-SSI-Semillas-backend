package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DidiEmmitCredentialResponse {
    private String status;
    private DidiCredential data;

    private String errorCode;//when error
    private String message;//when error

}