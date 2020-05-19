package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DidiAppUserDto {
    private Long dni;
    private String did;

    public DidiAppUserDto(Long dni, String did) {
        this.dni = dni;
        this.did = did;
    }
}