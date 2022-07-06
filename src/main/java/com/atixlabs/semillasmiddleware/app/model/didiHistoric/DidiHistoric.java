package com.atixlabs.semillasmiddleware.app.model.didiHistoric;

import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
/**
 * @deprecated (Definir que hacer con esta clase al corto plazo)
 */
@Entity
@Getter
@Setter
@Table
@Deprecated(forRemoval = true)
public class DidiHistoric extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idDidiReceptor;

    private Long idPerson;

    private boolean isActive; //Only one did is active at the same time
}
