package com.atixlabs.semillasmiddleware.security.service;

import com.atixlabs.semillasmiddleware.security.configuration.CustomUser;
import com.atixlabs.semillasmiddleware.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("permission")
public class CustomPermissionEvaluator  implements CustomPermissionEvaluatorInterface {

    @Autowired
    public CustomPermissionEvaluator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;

    public boolean canUpdate(CustomUser userMakingRequest, Long id, String rol) {
        boolean state;
        if(userMakingRequest.getId() != id){
            state = false;
        }else{
            state = true;
        }
        if(hasRole(userMakingRequest, rol)){state = true;}

        return state;
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
