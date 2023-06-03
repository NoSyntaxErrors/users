package com.factory.users.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static io.jsonwebtoken.Jwts.*;

@Component
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;
    private static final long EXPIRATION_TIME = 600_000;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!validateJwt(header, response)) {
            filterChain.doFilter(request, response);
            return;
        }

        User userDetails = jwtTokenUtil.getUserFromJwt(header.split(" ")[1].trim());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean validateJwt(String header, HttpServletResponse response) {

        if (header == null || !header.startsWith("Bearer ")) {
            return false;
        }

        String token = header.split(" ")[1].trim();

        try {
            Jwt<Header, Claims> jwt = parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8))).build().parse(token);
            Claims claims = jwt.getBody();

            return claims.getIssuer().equalsIgnoreCase(claims.getAudience());

        } catch (MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            return false;
        } catch (ExpiredJwtException ex) {


            DecodedJWT jwt = JWT.decode(token);

            Date now = new Date();

            String refreshToken = JWT.create()
                    .withExpiresAt(new Date(now.getTime() + EXPIRATION_TIME))
                    .withIssuedAt(now)
                    .withIssuer(jwt.getIssuer())
                    .withAudience(jwt.getAudience().get(0))
                    .withClaim("name", jwt.getClaim("name").asString())
                    .withClaim("p4ssword", jwt.getClaim("p4ssword").asString())
                    .withClaim("uuid", UUID.randomUUID().toString())
                    .sign(Algorithm.HMAC256(jwtSecretKey.getBytes(StandardCharsets.UTF_8)));


            response.setHeader("RefreshToken", refreshToken);


            return false;
        }
    }
}
