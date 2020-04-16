package com.atixlabs.semillasmiddleware.app.bondarea.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table
public class Loan {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dniPerson;

    private Boolean isActive; //Active or Inactive

    private String idBondareaLoan;

    //TODO private LocalDate(la hora no seria necesaria) -> date of payment ?

}
