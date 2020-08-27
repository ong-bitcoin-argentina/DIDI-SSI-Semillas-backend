package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model;


import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RejectReason;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
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
    @Enumerated(value = EnumType.STRING)
    private RejectReason rejectReason;

    private LocalDate date;
    private LocalDate reviewDate;

    private String rejectionObservations;

    protected IdentityValidationRequest(){}

    public IdentityValidationRequest(Long dni,
                                     String did,
                                     String email,
                                     String phone,
                                     String name,
                                     String lastName,
                                     RequestState requestState,
                                     LocalDate date){
        this.dni = dni;
        this.did = did;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.lastName = lastName;
        this.requestState = requestState;
        this.date = date;
    }

    @Override
    public String toString() {
        return "IdentityValidationRequest{" +
                "id=" + id +
                ", dni=" + dni +
                ", did='" + did + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", requestState=" + requestState +
                ", rejectReason=" + rejectReason +
                ", date=" + date +
                ", rejectionObservations='" + rejectionObservations + '\'' +
                '}';
    }
}
