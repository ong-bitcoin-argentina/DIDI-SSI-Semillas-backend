package com.atixlabs.semillasmiddleware.app.exceptions;

import lombok.Getter;

@Getter
public class PersonDoesNotExists extends Exception {

  public PersonDoesNotExists(String s) {}
}
