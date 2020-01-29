package com.atixlabs.semillasmiddleware.security.dto;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class JwtResponse implements Serializable {

    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

}