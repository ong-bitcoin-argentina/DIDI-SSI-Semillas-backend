package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "credential")
@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDidiIssuer;

    private Long idDidiReceptor;

    @Column(unique = true)
    private Long idDidiCredential;

    private Long idHistorical;

    private LocalDateTime dateOfIssue;

    private LocalDateTime dateOfExpiry;

    /**
     *
     */
    //private Long idRelatedCredential;

    @ManyToOne
    private Person beneficiary;

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", idDidiIssuer=" + idDidiIssuer +
                ", idDidiReceptor=" + idDidiReceptor +
                ", idDidiCredential=" + idDidiCredential +
                ", idHistorical=" + idHistorical +
                ", dateOfIssue=" + dateOfIssue +
                ", dateOfExpiry=" + dateOfExpiry +
                //", idRelatedCredential=" + idRelatedCredential +
                ", beneficiary=" + beneficiary +
                '}';
    }
}
