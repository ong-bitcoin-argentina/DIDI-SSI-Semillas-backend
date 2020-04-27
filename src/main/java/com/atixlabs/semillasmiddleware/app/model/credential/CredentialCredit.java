package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
@ToString
public class CredentialCredit extends Credential {

    private Long idCredit;

    private String creditName;

    private Long idGroup;

    //presidente, secretario y tesorero, se obtiene a partir de la api de Bondarea
    private String groupName;

    private String rol;

    private String currentCycle;

    private String creditState;

    private Double amount; //TODO revisar el tipo para monto. Para dinero deberiamos usar BigDecimal

    //private String beneficiaryDocumentType;

    //private Long dniBeneficiary;

    private LocalDateTime dateOfIssue;//NO VAN ACÁ SINO EN CADA CREDENCIAL HIJA QUE CORRESPONDA.

    private LocalDateTime dateOfExpiry;//NO VAN ACÁ SINO EN CADA CREDENCIAL HIJA QUE CORRESPONDA.

}
