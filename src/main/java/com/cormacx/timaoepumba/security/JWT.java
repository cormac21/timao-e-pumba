package com.cormacx.timaoepumba.security;

import com.cormacx.timaoepumba.entities.user.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class JWT {

    private static long EXPIRATION_TIME = 5 * 24 * 60 * 60 * 1000; //5 dias
    private static long ADMIN_EXPIRATION_TIME = 8 * 60 * 60 * 1000; //1 dia util

    private static String SECRET = "m!cr0Bl0g S3rveR";
    private static final String PREFIX = "Bearer";

    public static String token(UserEntity userEntity) {
        final var authorities = userEntity.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        final var expiration = System.currentTimeMillis() + (
                authorities.contains("ROLE_ADMIN") ? ADMIN_EXPIRATION_TIME : EXPIRATION_TIME);

        return PREFIX + " " + Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .setSubject(userEntity.getEmail())
                .setIssuer("timao-e-pumba")
                .addClaims(Map.of("userId", userEntity.getId()))
                .addClaims(Map.of("authorities", authorities))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static Authentication extract(HttpServletRequest req) {
        final var header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(PREFIX)) return null;

        final var token = header.replace(PREFIX, "").trim();

        final var claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        final var user = claims.getSubject();
        final var authorities = claims.get("authorities", ArrayList.class)
                .stream().map(o -> new SimpleGrantedAuthority(o.toString()))
                .toList();

        return user == null ? null :
                UsernamePasswordAuthenticationToken.authenticated(user, claims.get("userId", Long.class), authorities);
    }

}
