package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DidiGetTemplateResponse {
    private String status;
   // private String data;

    private String errorCode;//when error
    private String message;//when error
}
