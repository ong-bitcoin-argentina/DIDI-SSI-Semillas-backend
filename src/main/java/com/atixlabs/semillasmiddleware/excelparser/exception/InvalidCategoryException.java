package com.atixlabs.semillasmiddleware.excelparser.exception;

public class InvalidCategoryException extends Throwable {
    public InvalidCategoryException(String invalidCategory){
        super("No existe la categoría: " + invalidCategory);
    }
}
