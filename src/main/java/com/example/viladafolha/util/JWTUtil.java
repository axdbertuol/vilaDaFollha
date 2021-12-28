package com.example.viladafolha.util;

import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.transport.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JWTUtil {
    private final String secret;
    private final Long expiration;


    public JWTUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") Long expiration
                   ) {
        this.secret = secret;
        this.expiration = expiration;
    }



    public String getSubject(String token) {
        var parsed =
                Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

        return parsed.getBody().getSubject();
    }



	public JwtDTO generateToken(String email) {
		Date tokenExpiration = new Date(System.currentTimeMillis() + expiration);
		String token = Jwts.builder()
		.setSubject(email)
		.setExpiration(tokenExpiration)
		.signWith(SignatureAlgorithm.HS512, secret).compact();
		return new JwtDTO("Bearer", token, tokenExpiration.getTime());
	}

    public String resolveToken(HttpServletRequest request) {
        var bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new SignatureException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT claims string is empty.");
        }
    }


}
