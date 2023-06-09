package com.factory.users.service;

import com.factory.users.model.exception.UnauthorizedAccessException;
import com.factory.users.repositories.UserRegisteredRepository;
import com.factory.users.repositories.entities.UserRegistered;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    private final UserRegisteredRepository userRegisteredRepository;

    private static final long EXPIRATION_TIME = 600_000;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    public String generateJwt(UserRegistered userRegistered) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userRegistered.getName());
        claims.put("p4ssword", Base64.getEncoder().encodeToString(userRegistered.getPassword().getBytes(StandardCharsets.UTF_8)));
        claims.put("uuid", UUID.randomUUID());


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(userRegistered.getEmail())
                .setAudience(userRegistered.getEmail())
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

    }

    public User getUserFromJwt(String token){

        Jwt jwt = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8))).build().parse(token);
        Claims claims = (Claims) jwt.getBody();

        String password = claims.get("p4ssword", String.class);
        String email = claims.getAudience();

        UserRegistered userRegistered = userRegisteredRepository.getUserRegisteredByEmail(email);
        String desEncryptedPassword = new String(Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8)));

        if(desEncryptedPassword.equalsIgnoreCase(userRegistered.getPassword())){

            return new User(userRegistered.getEmail(), "", Collections.emptyList());
        }else{
            throw new UnauthorizedAccessException("Usuario y/o contraseña invalidos");
        }

    }
/**
    @Deprecated
    private String getEncryptedPassword(String password) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("SECRET_KEY".getBytes(StandardCharsets.UTF_8), "AES"));

        return new String(cipher.doFinal(password.getBytes()));
    }

    @Deprecated
    private String getDesEncryptedPassword(String encryptedPassword) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec("SECRET_KEY".getBytes(StandardCharsets.UTF_8), "AES"));

        return new String(cipher.doFinal(encryptedPassword.getBytes()));
    }
**/
}
