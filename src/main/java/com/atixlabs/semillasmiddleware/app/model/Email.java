package com.atixlabs.semillasmiddleware.app.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Email {

    private String to;
    private String template;
    private String subject;



}
