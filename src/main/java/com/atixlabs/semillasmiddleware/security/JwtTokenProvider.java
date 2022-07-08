package com.atixlabs.semillasmiddleware.security;

import com.atixlabs.semillasmiddleware.security.util.JwtTokenControlUtil;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    private JwtTokenControlUtil jwtTokenControlUtil;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(UserDetails userDetails){
        return doGenerateToken(userDetails.getUsername());
    }

    private byte[] getSecret() {
        return (secret).getBytes(StandardCharsets.UTF_8);
    }

    private String doGenerateToken(String subject) {

        Date now = DateUtil.getDateNow();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String token =  Jwts.builder()
                        .setSubject(subject)
                        .setIssuedAt(now)
                        .setExpiration(expiryDate)
                        .signWith(SignatureAlgorithm.HS512, this.getSecret())
                        .compact();
        jwtTokenControlUtil.setToken(subject,token);
        return token;
    }


    public Boolean validateToken(String token, UserDetails userDetails){
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token))&&(jwtTokenControlUtil.isTokenValid(token));
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(DateUtil.getDateNow());
    }

    public boolean revoqueToken(String username) {
         return jwtTokenControlUtil.revoqueToken(username);
    }

}


