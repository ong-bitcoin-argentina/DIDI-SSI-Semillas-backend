package com.atixlabs.semillasmiddleware.security.controller;

import com.atixlabs.semillasmiddleware.security.dto.JwtRequest;
import com.atixlabs.semillasmiddleware.security.dto.JwtResponse;
import com.atixlabs.semillasmiddleware.security.JwtTokenProvider;
import com.atixlabs.semillasmiddleware.security.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        logger.info("__ __ __ __ __ " +authenticationRequest.getUsername()+ authenticationRequest.getPassword());
        this.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        logger.info("authenticationRequest.getUsername()"+authenticationRequest.getUsername());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        logger.info("__ __ __ __ __ ");
        final String token = jwtTokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            logger.info("USER_DISABLED", e);
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            logger.info("INVALID_CREDENTIALS", e);
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

