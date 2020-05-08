package com.atixlabs.semillasmiddleware.app.didi.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DidiCreateCredentialBody {

    private boolean split;
    private boolean deleted;

    private LocalDateTime createdOn;
    private String _id;
    private String templateId;
    private String __v;

    private DidiData didiData;

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
      "split": true,
      "deleted": false,
      "createdOn": "2020-05-07T18:15:45.794Z",
      "_id": "5eb450513ac4af0256d20535",
      "microCredentials": [],
      "jwts": [],
      "templateId": "5e99c3cb9cc12b3d979357b5",
      "__v": 0
    }
  ]
}
 */