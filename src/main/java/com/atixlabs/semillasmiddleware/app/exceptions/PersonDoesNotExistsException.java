package com.atixlabs.semillasmiddleware.app.exceptions;

import lombok.Getter;

@Getter
public class PersonDoesNotExistsException extends RuntimeException{

  public PersonDoesNotExistsException(String s) {
    super(s);
  }
}
