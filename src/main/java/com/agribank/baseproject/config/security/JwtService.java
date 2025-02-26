package com.agribank.baseproject.config.security;

import com.agribank.baseproject.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRole(String token){
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles");
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

     public String generateToken(
             Map<String, Object> extraClaims,
             User userDetails
     ) {
         List<String> roleList = new ArrayList<>();
         userDetails.getRoles().stream().map(role -> roleList.add(role.getName())).toList();

         extraClaims.put("roles", roleList);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
     }

     public boolean isTokenValid(String token, UserDetails userDetails) {
        final String tokenUsername = extractUsername(token);
        return tokenUsername.equals(userDetails.getUsername()) && !isTokenExpired(token);
     }

     private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
     }

     private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
     }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
