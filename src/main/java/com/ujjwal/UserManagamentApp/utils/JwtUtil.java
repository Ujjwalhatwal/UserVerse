package com.ujjwal.UserManagamentApp.utils;

import com.ujjwal.UserManagamentApp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final Long expirationTime = 3600000L;


    private <T>T extractClaim(String jwtToken, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // method to extract userId from jwt token
    public Long extractUserId(String jwtToken) {
        String userIdStr = extractClaim(jwtToken,claims -> claims.get("userId", String.class));
        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }

    // method to extract username from jwt token
    private String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    // method to extract expiration date from jwt token
    private Date extractExpiration(String jwtToken) {

        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public String generateToken(User user) {

        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
       Map<String, Object> claims = new HashMap<>(extraClaims);
         claims.put("userId", user.getUserId().toString());
         return Jwts.builder()
                 .addClaims(claims)
                 .setSubject(user.getUsername())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                 .signWith(getSignKey())
                 .compact();
    }
    public boolean isTokenValid(User userDetails, String jwtToken) {

        final Long userIdFromToken = extractUserId(jwtToken);
        final Long userIdFromUserDetails = userDetails.getUserId();
        return userIdFromToken != null && userIdFromToken.equals(userIdFromUserDetails) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

}
