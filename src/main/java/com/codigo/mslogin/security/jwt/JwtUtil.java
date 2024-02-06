package com.codigo.mslogin.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final String secretToken = "myToken";
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims); // claims.getExprition
    }
    public Claims getAllClaims(String token){
        return Jwts.parser().setSigningKey(secretToken).parseClaimsJws(token).getBody();
    }
    public Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }
    public String getUsername(String token){
        return getClaim(token, Claims::getSubject);
    }
    public Boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2))
                .signWith(SignatureAlgorithm.HS256, secretToken)
                .compact();
    }
    public String generateToken(String username, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }
    public Boolean validateToken(String token, String username){
        final String usernameToken = getUsername(token);
        return (usernameToken.equals(username) && !isTokenExpired(token));
    }
}
