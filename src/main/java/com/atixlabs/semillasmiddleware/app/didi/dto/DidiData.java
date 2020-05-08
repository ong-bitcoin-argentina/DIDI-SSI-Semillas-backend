package com.atixlabs.semillasmiddleware.app.didi.dto;

import java.util.ArrayList;

public class DidiData {
    private ArrayList<DidiAtomicData> cert;
    private ArrayList<DidiAtomicData> participant;
    private ArrayList<DidiAtomicData> others;
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