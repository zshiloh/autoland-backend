package com.proyecto.autoland.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.security.Key;

import org.springframework.stereotype.Component;

import com.proyecto.autoland.model.Usuario;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtUtil {

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerEmail(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(SECRET_KEY)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}