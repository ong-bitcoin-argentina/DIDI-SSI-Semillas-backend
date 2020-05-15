package com.atixlabs.semillasmiddleware.app.bondarea.model;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table
@ToString
public class Loan extends AuditableEntity {

    @PrePersist
    private void preSetValues(){
        if(this.pending == null)
            this.pending = false;
        if(this.hasCredential == null)
            this.hasCredential = false;
        if(this.statusDescription == null)
            this.statusDescription = "";
    }


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dniPerson;

    private String idBondareaLoan; // ID del crédito individual. Para créditosgrupales representa el tramo del crédito grupal   (Ej. B26F5FKZ)

    private String tagBondareaLoan; // Nombre del producto de préstamo (Ej. Recurrentes) //TODO credit type

    private String statusDescription; // Estado del préstamo (Ej. Preparación, Activo, Finalizado)

    private int status; // Estado numérico del préstamo (Ej.0=Preparación, 55= Activo, 60=finalizado)

    private String statusFullDescription; // Deccripcion de estado ?

    //@Column(columnDefinition = "boolean default false")
    private Boolean pending;

    private String idProductLoan;  //ID de producto de préstamo (Ej.  B26F5FKZ)

    private String idGroup; // ID del   crédito grupal y su estado (Ej.55-B26F5FKZ)

    private String cycleDescription; // Nombre asignado al crédito (Ej. Ciclo 2)

    private LocalDate creationDate; // Fecha de otorgamiento cuentas

    private String personName; // Nombre del solicitante del tramo (Ej. Perez, Juan)

    private String userId; // ID del solicitante del tramo (Ej. B26F5FKZ)

    private Float amount; // Monto del crédito del tramo (Ej. 10000)

    private LocalDate dateFirstInstalment; // Fecha de primera cuota

    private Float expiredAmount; // Saldo vencido del crédito individual, compuesto por capital, intereses, seguros y cargos (Ej. 1845.24)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedTime;

    private Boolean hasCredential;



    public Loan(BondareaLoanDto loanDto) {
        this.dniPerson = loanDto.getDni();

        this.idBondareaLoan = loanDto.getIdBondareaLoan();

        this.tagBondareaLoan = loanDto.getTagBondareaLoan();

        this.statusDescription =  loanDto.getStatusDescription();

        this.status =   loanDto.getStatus();

        this.statusFullDescription =  loanDto.getStatusFullDescription();

        this.idProductLoan =   loanDto.getIdProductLoan();

        this.idGroup =   loanDto.getIdGroup();

        this.cycleDescription =   loanDto.getCycle();

        this.personName =   loanDto.getPersonName();

        this.userId =   loanDto.getUserId();

        this.amount =  loanDto.getAmount();

        this.expiredAmount =   loanDto.getExpiredAmount();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        if(loanDto.getDateFirstInstalment() != null) {
            this.dateFirstInstalment = LocalDate.parse(loanDto.getDateFirstInstalment(), formatter);
        }

        if(loanDto.getCreationDate() != null) {
            this.creationDate = LocalDate.parse(loanDto.getCreationDate(), formatter);
        }

    }

    public void merge(Loan loanToUpdate){

        this.dniPerson = loanToUpdate.getDniPerson();

        //this.isActive = loanToUpdate.getIsActive();
        //this.isDeleted = loanToUpdate.getIsDeleted();
        //this.pending = loanToUpdate.getPending();

        this.idBondareaLoan = loanToUpdate.getIdBondareaLoan();

        this.tagBondareaLoan = loanToUpdate.getTagBondareaLoan();

        this.statusDescription = loanToUpdate.getStatusDescription();

        this.status = loanToUpdate.getStatus();

        this.statusFullDescription = loanToUpdate.getStatusFullDescription();

        this.idProductLoan = loanToUpdate.getIdProductLoan();

        this.idGroup = loanToUpdate.getIdGroup();

        this.cycleDescription = loanToUpdate.getCycleDescription();

        this.creationDate = loanToUpdate.getCreationDate();

        this.personName = loanToUpdate.getPersonName();

        this.userId = loanToUpdate.getUserId();

        this.amount = loanToUpdate.getAmount();

        this.dateFirstInstalment = loanToUpdate.getDateFirstInstalment();

        this.expiredAmount = loanToUpdate.getExpiredAmount();

        this.modifiedTime = loanToUpdate.getModifiedTime();

    }

    public Loan() {}
}
