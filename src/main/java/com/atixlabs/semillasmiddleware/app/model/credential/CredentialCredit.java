package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialCredit extends Credential {

    private String idBondareaCredit;

    private LocalDate creationDate;

    private String creditType; //TODO  Credito semilla / escolar / estacional / individual / de motos/de min. desarrolo social

    private String idGroup;

    private String currentCycle;

    private int totalCycles;

    private int amountExpiredCycles;

    private String creditState;

    private LocalDate finalizedTime;

    private Float expiredAmount; //TODO ? can have money in favour

}
