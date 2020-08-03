package com.atixlabs.semillasmiddleware.app.sancor.model;


import com.atixlabs.semillasmiddleware.excelparser.app.dto.SancorPolicyRow;
import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import com.atixlabs.semillasmiddleware.security.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table
@ToString
@Slf4j
//  Descripción Ramo  Producto	Póliza	Cliente Poliza	Nombre Cliente Póliza	Certificado	Cliente Certificado	Ini. Vigencia Cert. Ori.	Fin Vigencia Cert.	Nombre Cliente Cert.	Domicilio
public class SancorPolicy extends AuditableEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

   // @CreatedBy
    //private User user;

  //  @CreatedDate
   // private Instant createdDate;

    //@LastModifiedDate
    //private Instant updateDate;

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




}
