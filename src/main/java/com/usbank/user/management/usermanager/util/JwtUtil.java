package com.usbank.user.management.usermanager.util;

import com.usbank.user.management.usermanager.model.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Date;


@Component
public class JwtUtil {

    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${userManger.app.jwtTokenName}")
    private String tokenName;

    @Value("${userManger.app.jwtSecret}")
    private String jwtSecret;

    @Value("${userManger.app.jwtExpirationMs}")
    private long jwtExpirationMs;


    public String getJwtFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token!= null) {
            return token;
        } else {
            return null;
        }
    }

    public String generateToken(User user) {
        Claims claims = Jwts.claims()
                .setSubject(user.getUsername())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs));
        claims.put("username", user.getUsername() + "");
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Could not get all claims Token from passed token");
            claims = null;
        }
        return claims;
    }

    public String getUserNameFromToken(String token){
       return getAllClaimsFromToken(token).getSubject();
    }

}
