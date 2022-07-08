package com.atixlabs.semillasmiddleware.security.service;

import com.atixlabs.semillasmiddleware.security.configuration.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("permission")
public class CustomPermissionEvaluator  implements CustomPermissionEvaluatorInterface {

    @Autowired
    public CustomPermissionEvaluator(){/* ** */ }

    public boolean canUpdate(CustomUser userMakingRequest, Long id, String rol) {
        return ((userMakingRequest.getId().equals(id)) && hasRole(userMakingRequest, rol));
    }

    public boolean hasRole(CustomUser principal, String rol) {
        for (GrantedAuthority grantedAuth : principal.getAuthorities()) {
            if (grantedAuth.getAuthority().contains(rol)) {
                return true;
            }
        }
        return false;
    }

}
