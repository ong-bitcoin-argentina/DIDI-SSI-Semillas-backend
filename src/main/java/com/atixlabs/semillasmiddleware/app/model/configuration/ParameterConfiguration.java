package com.atixlabs.semillasmiddleware.app.model.configuration;

import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class ParameterConfiguration extends AuditableEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String configurationName;

    private Float expiredAmountMax;


}
