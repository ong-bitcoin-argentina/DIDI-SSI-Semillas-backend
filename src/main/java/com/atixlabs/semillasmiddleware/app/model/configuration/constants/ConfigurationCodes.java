package com.atixlabs.semillasmiddleware.app.model.configuration.constants;

public enum ConfigurationCodes {

    MAX_EXPIRED_AMOUNT("MaxExpiredAmount");


    private String code;

    ConfigurationCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
