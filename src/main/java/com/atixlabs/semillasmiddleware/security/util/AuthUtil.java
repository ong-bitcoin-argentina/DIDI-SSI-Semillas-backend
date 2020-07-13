package com.atixlabs.semillasmiddleware.security.util;

import com.atixlabs.semillasmiddleware.security.configuration.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthUtil {

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        if(customUser!=null) {
            return customUser.getUsername();
        }

        return null;
    }


}
