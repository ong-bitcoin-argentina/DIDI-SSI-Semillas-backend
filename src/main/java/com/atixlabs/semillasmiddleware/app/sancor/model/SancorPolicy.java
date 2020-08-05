package com.atixlabs.semillasmiddleware.app.sancor.model;


import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table
@ToString
@Slf4j
public class SancorPolicy extends AuditableEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long certificateClientDni;

    @Column(length = 150)
    private String branchDescription; //Descripción Ramo

    private Long idProduct;//Producto

    private Long policyNumber;// Póliza

    private String policyClient;// Cliente Poliza

    private String policyClientName;// Nombre Cliente Póliza

    private Long certificateNumber;// Certificado

    private String certificateClient; //Cliente Certificado

    private LocalDate validityFrom;// 	Ini. Vigencia Cert. Ori.

    private LocalDate validityTo;// Fin Vigencia Cert.

    private String certificateClientName;// Nombre Cliente Cert.

    private String certificateClientAddress;// Domicilio

    private boolean needReview = false;


    public SancorPolicy merge(SancorPolicy sancorPolicyNewInfo){
        this.needReview = !this.equals(sancorPolicyNewInfo);
        this.branchDescription = sancorPolicyNewInfo.branchDescription;
        this.idProduct = sancorPolicyNewInfo.getIdProduct();
        this.policyNumber = sancorPolicyNewInfo.getPolicyNumber();
        this.policyClient = sancorPolicyNewInfo.getPolicyClient();
        this.policyClientName = sancorPolicyNewInfo.getPolicyClientName();
        this.certificateNumber = sancorPolicyNewInfo.getCertificateNumber();
        this.certificateClient = sancorPolicyNewInfo.getCertificateClient();
        this.validityFrom = sancorPolicyNewInfo.getValidityFrom();
        this.validityTo = sancorPolicyNewInfo.getValidityTo();
        this.certificateClientName = sancorPolicyNewInfo.getCertificateClientName();
        this.certificateClientAddress = sancorPolicyNewInfo.getCertificateClientAddress();



        return this;
    }

    @Override
    public boolean equals(Object object){
        if(object == null)
            return false;

        if(!object.getClass().equals(this.getClass()))
            return super.equals(object);

        SancorPolicy newSancorPolicy = (SancorPolicy) object;

        log.info(this.getHash()+" -- "+newSancorPolicy.getHash());
        return this.getHash().equals(newSancorPolicy.getHash());
        //return (this.status.equals(newLoan.getStatus()) && this.cycleDescription.equals(newLoan.getCycleDescription()) && (this.expiredAmount.compareTo(newLoan.getExpiredAmount()) == 0));
    }

    public String getHash(){
        StringBuilder hashBuilder = new StringBuilder();
        //Fix part
        hashBuilder.append(this.policyNumber);
        hashBuilder.append(this.certificateNumber);

        String hash =  hashBuilder.toString();

        return hash !=null ? hash : "";
    }

}
