package com.atixlabs.semillasmiddleware.app.didi.dto;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@Getter
public class DidiCredentialData {
    private ArrayList<DidiCredentialDataElem> cert;
    private ArrayList<ArrayList<DidiCredentialDataElem>> participant;
    private ArrayList<DidiCredentialDataElem> others;

    public DidiCredentialData(Credential credential){
        this.cert = new ArrayList<>();
        cert.add(new DidiCredentialDataElem("CERTIFICADO O CURSO", credential.getCredentialCategory()));
        cert.add(new DidiCredentialDataElem("DNI", credential.getBeneficiaryDni().toString()));

        this.participant = new ArrayList<>();

        ArrayList<DidiCredentialDataElem> part = new ArrayList<>();

        //DID + NOMBRE + APELLIDO are mandatory fields
        part.add(new DidiCredentialDataElem("DID", credential.getIdDidiReceptor()));
        part.add(new DidiCredentialDataElem("NOMBRE", credential.getBeneficiaryFirstName()));
        part.add(new DidiCredentialDataElem("APELLIDO", credential.getBeneficiaryLastName()));

        participant.add(part);

        this.others = new ArrayList<>();
    }

    @Override
    public String toString() {
        return  "{" +
                " \"cert\":"+cert+
                ",\"participant\":"+participant+""+
                ",\"others\":"+others+
                "}";
    }
}



/*
{
  "status": "success",
  "data": [
    {
      "data": {
        "cert": [
          {
            "value": "test",
            "_id": "5eb450513ac4af0256d20536",
            "name": "CERTIFICADO O CURSO"
          }
        ],
        "participant": [
          [
            {
              "value": "123456789",
              "_id": "5eb450513ac4af0256d20537",
              "name": "DID"
            },
            {
              "value": "Pablo",
              "_id": "5eb450513ac4af0256d20538",
              "name": "NOMBRE"
            },
            {
              "value": "Picazzo",
              "_id": "5eb450513ac4af0256d20539",
              "name": "APELLIDO"
            }
          ]
        ],
        "others": []
      },

 */