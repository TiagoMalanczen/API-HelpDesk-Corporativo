package com.com.empresa.helpdesk.config;

import com.com.empresa.helpdesk.data.model.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.key}")
    private String key;

    public String gerarToken(Authentication authentication) {
        UsuarioEntity usuario = (UsuarioEntity) authentication.getPrincipal();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("role", usuario.getPerfil().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSingnedKey())
                .compact();
    }

    private SecretKey getSingnedKey(){
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
    public boolean isTokenValid(String token){
        try {
            getClaims(token);
            return true;
        }
        catch (Exception e){
            return  false;
        }
    }
    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getSingnedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String getUserName(String tokem){
        return getClaims(tokem).getSubject();
    }
}
