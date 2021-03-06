package com.atixlabs.semillasmiddleware.app.model.credential;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CredentialEntrepreneurship extends Credential {

    // Comercio, Producción,Servicio
    private String entrepreneurshipType; //TODO enum or new class?

    private Integer startActivity; //storing only the year

    private String mainActivity;

    private String entrepreneurshipName;

    private String entrepreneurshipAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endActivity;


    public CredentialEntrepreneurship(CredentialEntrepreneurship credentialEntrepreneurship){
        super(credentialEntrepreneurship);
        this.entrepreneurshipType = credentialEntrepreneurship.entrepreneurshipType;
        this.startActivity = credentialEntrepreneurship.startActivity;
        this.mainActivity = credentialEntrepreneurship.mainActivity;
        this.entrepreneurshipName = credentialEntrepreneurship.entrepreneurshipName;
        this.entrepreneurshipAddress = credentialEntrepreneurship.entrepreneurshipAddress;
        this.endActivity = credentialEntrepreneurship.endActivity;
    }

}
