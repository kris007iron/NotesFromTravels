package com.kris007iron.NotesFromTravels.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final Key SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String generateToken(String login, long expiration_time) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration_time);

        Map<String, Object> claims = new HashMap<>();
        claims.put("login", login);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        Date expirationDate = getExpirationDateFromToken(token);
        assert expirationDate != null;
        if (expirationDate.before(new Date())) {
            return false;
        }
        String username = getLoginFromToken(token);
        return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
    }

    public static String getLoginFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("login", String.class);
        } catch (ExpiredJwtException e) {
            // Token expired
            return "";
        }
    }

    public static Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("exp", Date.class);
        } catch (ExpiredJwtException e) {
            // Token expired
            return null;
        }
    }

    //either JWT or OAuth2
    public static String extractLoginFromAuth(HttpServletRequest request){
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            String token = authHeader.substring(7);
            return JwtService.getLoginFromToken(token);
        }
        return null;
    }
}
