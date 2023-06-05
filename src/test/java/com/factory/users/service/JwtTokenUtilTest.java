package com.factory.users.service;

import com.factory.users.model.exception.UnauthorizedAccessException;
import com.factory.users.repositories.UserRegisteredRepository;
import com.factory.users.repositories.entities.UserRegistered;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenUtilTest {

    @Mock
    private UserRegisteredRepository userRegisteredRepository;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;
    private static final long EXPIRATION_TIME = 600_000;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecretKey", "ZnJhbmNpc2NvYW5kcmVzc2FsYXNiYXJyYWxlcw==");

    }

    @Test
    void generateJwtValid() {
        UserRegistered userRegistered = UserRegistered.builder()
                .email("fsalaslazcano@gmail.com")
                .password("password")
                .name("Francisco Salas Lazcano")
                .build();
        when(userRegisteredRepository.getUserRegisteredByEmail(userRegistered.getEmail()))
                .thenReturn(userRegistered);

        String jwtToken = jwtTokenUtil.generateJwt(userRegistered);

        assertNotNull(jwtToken);
    }

    @Test
    void getUserFromJwtWithCorrectCredentials() {

        String email = "fsalaslazcano@example.com";
        String password = "password";
        String encryptedPassword = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));

        UserRegistered userRegistered = UserRegistered.builder()
                .email(email)
                .password(encryptedPassword)
                .build();
        when(userRegisteredRepository.getUserRegisteredByEmail(email))
                .thenReturn(userRegistered);

        String jwtToken = jwtTokenUtil.generateJwt(userRegistered);

        User userDetails = jwtTokenUtil.getUserFromJwt(jwtToken);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRegisteredRepository, times(1)).getUserRegisteredByEmail(email);
    }

    @Test
    void getUserFromJwtWithInCorrectCredentials() {

        String email = "fsalaslazcano@gmail.com";
        String password = "password";

        UserRegistered userRegistered = UserRegistered.builder()
                .email(email)
                .password(Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)))
                .build();
        when(userRegisteredRepository.getUserRegisteredByEmail(email))
                .thenReturn(userRegistered);

        UserRegistered userRegisteredJwt = UserRegistered.builder()
                .email(email)
                .password(Base64.getEncoder().encodeToString("password1".getBytes(StandardCharsets.UTF_8)))
                .build();
        String jwtToken = jwtTokenUtil.generateJwt(userRegisteredJwt);

        assertThrows(UnauthorizedAccessException.class, () -> jwtTokenUtil.getUserFromJwt(jwtToken));

    }

    @Test
    void getUserFromJwtThrowsMalformedJwtException() {
        String invalidJwtToken = "invalidToken";

        assertThrows(MalformedJwtException.class, () -> jwtTokenUtil.getUserFromJwt(invalidJwtToken));

        verify(userRegisteredRepository, never()).getUserRegisteredByEmail(anyString());
    }


    @Test
    void getUserFromJwtThrowsExpiredException() {
        String invalidJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiRnJhbmNpc2NvIFNhbGFzIExhemNhbm8iLCJ1dWlkIjoiYWU3OGUyODAtODVlNi00N2NmLTk3YmYtZDAxZGYxNTBmNDM4IiwicDRzc3dvcmQiOiJSbkpoYm1OcGMyTnZPVFU9IiwiaWF0IjoxNjg1ODQ4NTI2LCJpc3MiOiJmc2FsYXNsYXpjYW5vQGdtYWlsLmNvbSIsImF1ZCI6ImZzYWxhc2xhemNhbm9AZ21haWwuY29tIiwiZXhwIjoxNjg1ODQ5MTI2fQ.fVeu-687C_BE67gOXWihxj1K76MnPyXzQXEJrvTM9eU";

        assertThrows(ExpiredJwtException.class, () -> jwtTokenUtil.getUserFromJwt(invalidJwtToken));

        verify(userRegisteredRepository, never()).getUserRegisteredByEmail(anyString());
    }

}
