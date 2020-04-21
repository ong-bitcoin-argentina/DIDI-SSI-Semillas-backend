package com.atixlabs.semillasmiddleware.excelparser.app.exception;

public class InvalidCategoryException extends Throwable {
    public InvalidCategoryException(String invalidCategory){
        super("No existe la categoría: " + invalidCategory);
    }
}
