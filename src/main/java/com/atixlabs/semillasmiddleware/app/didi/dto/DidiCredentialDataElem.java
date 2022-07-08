package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;

@Getter
public class DidiCredentialDataElem {
    private String name;
    private String value;
    private String id;

    public DidiCredentialDataElem(String name, String value){
        this.name = name;
        this.value = value;
    }

    public DidiCredentialDataElem(String name, String value, String id){
        this.name = name;
        this.value = value;
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                " \"value\":\""+value+"\""+
                ",\"name\":\""+name+"\""+
          //    ",\"_id\":\""+ _id+"\""+
               "}";
    }


}
