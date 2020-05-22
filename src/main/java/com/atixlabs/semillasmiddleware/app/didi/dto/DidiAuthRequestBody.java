package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class DidiAuthRequestBody {
    private String name;
    private String password;

    public DidiAuthRequestBody(String username, String password) {
        this.name = username;
        this.password = password;
    }
}