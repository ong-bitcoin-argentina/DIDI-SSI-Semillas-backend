package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialPersonalData extends Credential{

    //Se mapea con Person? Misma data

    //Todo: Familiar y titular

    private String name;

    private String surname;

    private Long dniBeneficiary;

    private String gender;

    private LocalDate birthDate;
}
