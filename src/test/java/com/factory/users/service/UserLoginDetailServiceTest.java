package com.factory.users.service;

import com.factory.users.model.exception.UnauthorizedAccessException;
import com.factory.users.repositories.UserRegisteredRepository;
import com.factory.users.repositories.entities.UserRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserLoginDetailServiceTest {

    @Mock
    private UserRegisteredRepository userRegisteredRepository;

    @InjectMocks
    private UserLoginDetailServiceImpl userLoginDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUsernameWithCorrectCredentials() {
        String email = "fsalaslazcano@gmail.com";
        String password = "cGFzc3dvcmQ=";

        UserRegistered userRegistered = UserRegistered.builder()
                .email(email)
                .password(password)
                .build();
        when(userRegisteredRepository.getUserRegisteredByEmail(email))
                .thenReturn(userRegistered);

        User userDetails = userLoginDetailService.loadUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRegisteredRepository, times(1)).getUserRegisteredByEmail(email);
    }

    @Test
    void loadUsernameThrowsUnauthorizedAccess() {

        String email = "nonexistent@example.com";
        when(userRegisteredRepository.getUserRegisteredByEmail(email))
                .thenReturn(null);

        assertThrows(UnauthorizedAccessException.class, () -> userLoginDetailService.loadUsername(email));

        verify(userRegisteredRepository, times(1)).getUserRegisteredByEmail(email);
    }
}
