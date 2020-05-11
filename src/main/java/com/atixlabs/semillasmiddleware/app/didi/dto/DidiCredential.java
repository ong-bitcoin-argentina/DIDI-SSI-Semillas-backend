package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DidiCredential {
    private DidiCredentialData data;

    private boolean split;
    private boolean deleted;
    //private LocalDateTime createdOn;
    private String _id;


    private String templateId;
    private String __v;
}