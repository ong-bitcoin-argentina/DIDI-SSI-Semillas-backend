package com.atixlabs.semillasmiddleware.security;

import com.atixlabs.semillasmiddleware.security.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService; //TODO get user form db

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("URI [{}]", request.getRequestURI());
        log.info("Method [{}]", request.getMethod());
        try {
            String jwtToken = this.getJwtFromRequest(request);
            String username = getUserNameFromToken(jwtToken);

            log.info("username {}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("loadUserByUsername");
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

                if (Boolean.TRUE.equals(jwtTokenProvider.validateToken(jwtToken, userDetails))) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        chain.doFilter(request, response);
    }

    private String getUserNameFromToken(String jwtToken){
        try {
            return jwtTokenProvider.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            log.info("Imposible obtener JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("JWT Token Expirado", e);
        }
        return null;
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}