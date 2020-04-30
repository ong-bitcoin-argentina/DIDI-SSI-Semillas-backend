package com.atixlabs.semillasmiddleware.app.exceptions;

import com.atixlabs.semillasmiddleware.security.model.User;
import lombok.Getter;

@Getter
public class NoExpiredConfigurationExists extends Exception {


  public NoExpiredConfigurationExists(String s) {}
}
