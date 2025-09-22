package devdan.restful.resolver;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private final Key secretKey;
    private final long expirationMillis;

    public JwtUtil(String secret, long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMillis = expirationMillis;
    }

    public String generatedToken(String username){
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (1000 * 60 * 60);
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(exp)
                .setIssuer("devdan-app")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenWithExpiration(String username, long expirationMillis) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expirationMillis;
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(exp)
                .setIssuer("devdan-app")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetUsername(String token){
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getBody().getSubject();
        } catch (JwtException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token", e);
        }
    }

    public Long getExpirationTime(String token){
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getBody().getExpiration().getTime();
        }catch (JwtException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token", e);
        }
    }

}
