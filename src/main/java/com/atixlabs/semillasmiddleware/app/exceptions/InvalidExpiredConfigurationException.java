package com.atixlabs.semillasmiddleware.app.exceptions;

import lombok.Getter;

@Getter
public class InvalidExpiredConfigurationException extends Exception {


  public InvalidExpiredConfigurationException(String s) {
    super(s);
  }
}
