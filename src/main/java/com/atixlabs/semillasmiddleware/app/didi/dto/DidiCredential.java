package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DidiCredential {
    private DidiCredentialData data;

    private boolean split;
    private boolean deleted;
    private String id;


    private String templateId;
    private String v;
}