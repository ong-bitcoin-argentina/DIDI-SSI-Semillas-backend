package com.atixlabs.semillasmiddleware.app.bondarea.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BondareaLoanResponse {

    private String estado; // [correcto | error]
    private String error; // Descripción del tipo de error
    private String errorcode; // Código de error

    private List<BondareaLoan> loans;
}
