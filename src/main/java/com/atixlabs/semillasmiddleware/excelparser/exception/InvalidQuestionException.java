package com.atixlabs.semillasmiddleware.excelparser.exception;

public class InvalidQuestionException extends Exception {
    public InvalidQuestionException(String invalidQuestion){
        super("No existe la clave: " + invalidQuestion);
    }
}
