package com.atixlabs.semillasmiddleware.security.controller;

import com.atixlabs.semillasmiddleware.app.exceptions.UnknownErrorException;
import com.atixlabs.semillasmiddleware.security.configuration.CustomUser;
import com.atixlabs.semillasmiddleware.security.exceptions.InactiveUserException;
import com.atixlabs.semillasmiddleware.security.JwtTokenProvider;
import com.atixlabs.semillasmiddleware.security.dto.AuthenticatedUserDto;
import com.atixlabs.semillasmiddleware.security.dto.JwtAuthenticationResponse;
import com.atixlabs.semillasmiddleware.security.dto.JwtRequest;
import com.atixlabs.semillasmiddleware.security.service.JwtUserDetailsService;
import com.atixlabs.semillasmiddleware.security.service.UserPermissionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(
        origins = {"http://localhost:8080", "${didi.server.url}"},
        methods = {RequestMethod.GET, RequestMethod.POST})
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserPermissionsService userPermissionsService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            log.info(" -createAuthenticationToken "+authenticationRequest.getUsername());
            Authentication authentication = this.authenticate(authenticationRequest.getUsername().trim(), authenticationRequest.getPassword());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUser customUser = (CustomUser) authentication.getPrincipal();

            final String token = jwtTokenProvider.generateToken(customUser);
            log.info("Generated token [" + token + "] for user [" + authenticationRequest.getUsername() + "]");

            Optional<AuthenticatedUserDto> authenticatedUserDto;

            authenticatedUserDto = userPermissionsService.findUserAuthenticated(authenticationRequest.getUsername(), authenticationRequest.getPassword());


            if (authenticatedUserDto.isPresent()) {
                authenticatedUserDto.get().setAccessToken(token);
                return ResponseEntity.ok(authenticatedUserDto);
            } else {
                return ResponseEntity.ok(new JwtAuthenticationResponse(token));
            }
        } catch (InactiveUserException ex) {
            log.error("User [" + authenticationRequest.getUsername() + "] inactive");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Unable to login. Please contact the system administrator");
        }

    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.info("USER_DISABLED. Error: {}", e.getMessage());
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.info("INVALID_CREDENTIALS. Error: {}", e.getMessage());
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            log.info("UNKNOWN ERROR: {}", e.getMessage());
            throw new UnknownErrorException("UNKNOWN_ERROR", e);
        }
    }

    @GetMapping(value = "/isauth")
    public Boolean isAuth(){ return true; }

    @GetMapping(value = "/logout")
    public void logout(@AuthenticationPrincipal CustomUser usuarioActual) {
        log.info("Perform authentication Logout " + usuarioActual.getUsername());
        jwtTokenProvider.revoqueToken(usuarioActual.getUsername());
    }
}

