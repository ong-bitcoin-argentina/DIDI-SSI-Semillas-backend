package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model;


import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Table(name = "identity_validation")
@Entity
public class IdentityValidationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dni;
    private String did;
    private String email;
    private String phone;
    private String name;
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private RequestState requestState;

    private LocalDate date;

    private String revocationReason;

    protected IdentityValidationRequest(){}

    public IdentityValidationRequest(Long dni,
                                     String did,
                                     String email,
                                     String phone,
                                     String name,
                                     String lastName,
                                     RequestState requestState,
                                     LocalDate date,
                                     String revocationReason){
        this.dni = dni;
        this.did = did;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.lastName = lastName;
        this.requestState = requestState;
        this.date = date;
        this.revocationReason = revocationReason;
    }

}
