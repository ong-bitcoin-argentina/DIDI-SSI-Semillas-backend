package com.atixlabs.semillasmiddleware.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users", schema="security")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String phone;

    private String username;

    @JsonIgnore
    private String password;

    @JoinColumn(name = "id_role")
    @ManyToOne
    private Role role;

    private boolean active;

}
