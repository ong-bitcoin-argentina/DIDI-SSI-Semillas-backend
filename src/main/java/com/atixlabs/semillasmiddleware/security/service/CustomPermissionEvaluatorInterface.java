package com.atixlabs.semillasmiddleware.security.service;

import com.atixlabs.semillasmiddleware.security.configuration.CustomUser;

public interface CustomPermissionEvaluatorInterface {
    boolean hasRole(CustomUser principal, String rol);
}
