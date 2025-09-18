package com.frael.SecurityJWT.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {

    // Clave para la firma del token codificada en formato Base64 - indica que el
    // token es valido
    @Value(value = "${jwt.secret.key}")
    private String secretKey;

    @Value(value = "${jwt.time.expiration}")
    private String timeExpiration;

    public String generateAccessToken(String usarname) {

        return Jwts.builder().setSubject(usarname).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256).compact(); // HS256 compatible con HMAC

    }

    /**
     * Decodifica nuestra llave y la codifica a HMAC-SHA
     * 
     * @return
     */
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Valida si el token es valido
     * 
     * @apiNote Aqui se realiza solamente una conversión
     *          del token, si no hay error en el proceso devuelve True, caso
     *          contraio
     *          False.
     *          No hace una validación más profunda
     * 
     * @param token
     * @return
     */
    public boolean isTokenValid(String token) {

        try {
            // TODO: Repasar parseClaimsJws vs parseClaimsJwt
            Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
            log.info("token valido");
            return true;
        } catch (Exception e) {
            log.error("Token invalido".concat(e.getMessage()));
        }

        return false;
    }

    /**** Metodos para los Claims *****/

    /**
     * Obtener todos los claims del token
     */
    public Claims extractAllClaims(String token) {
        // NOTA parseClaimsJwt != parseClaimsJwt - 2025-09-16T23:20:25.182-05:00 ERROR 2528 --- [SecurityJWT] [nio-8085-exec-3] c.f.SecurityJWT.security.jwt.JwtUtils    : Token invalidoSigned Claims JWSs are not supported.
        return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Metodo generico para obtener un solo Claim
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
        Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    /**
     * Obtener usuario
     */
    public String getSubjectFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

}
