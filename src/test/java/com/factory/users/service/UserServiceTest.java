package com.factory.users.service;

import com.factory.users.model.*;
import com.factory.users.model.exception.ResourceException;
import com.factory.users.model.exception.UnauthorizedAccessException;
import com.factory.users.repositories.PhoneRepository;
import com.factory.users.repositories.UserRegisteredRepository;
import com.factory.users.repositories.entities.Phone;
import com.factory.users.repositories.entities.UserRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    private UserRegisteredRepository userRegisteredRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private JwtTokenUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUpNewUserSuccessful() {

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setEmail("fsalaslazcano@gmail.com");
        userSignUpRequest.setPassword("password");
        userSignUpRequest.setName("Francisco Salas Lazcano");

        List<PhoneReq> phones = new ArrayList<>();
        phones.add(PhoneReq.builder().number(945836647).citycode(2).countrycode("+56").build());
        userSignUpRequest.setPhones(phones);

        when(userRegisteredRepository.getUserRegisteredByEmail(userSignUpRequest.getEmail()))
                .thenReturn(null);

        UUID uuid = UUID.randomUUID();
        UserRegistered newUserRegistered = UserRegistered.builder()
                .id(uuid)
                .email(userSignUpRequest.getEmail())
                .password(userSignUpRequest.getPassword())
                .name(userSignUpRequest.getName())
                .lastLogin(LocalDateTime.now())
                .created(LocalDateTime.now())
                .isActive(true)
                .build();
        when(userRegisteredRepository.save(any(UserRegistered.class)))
                .thenReturn(newUserRegistered);

        when(jwtUtil.generateJwt(newUserRegistered))
                .thenReturn("mocked-jwt-token");

        UserSignUpResponse response = userService.signUpUser(userSignUpRequest);

        assertNotNull(response);
        assertEquals(newUserRegistered.getLastLogin(), response.getLastLogin());
        assertEquals(newUserRegistered.getCreated(), response.getCreated());
        assertEquals(newUserRegistered.getIsActive(), response.getIsActive());
        assertEquals(newUserRegistered.getId(), response.getId());

        verify(userRegisteredRepository, times(1)).getUserRegisteredByEmail(userSignUpRequest.getEmail());
        verify(userRegisteredRepository, times(1)).save(any(UserRegistered.class));
        verify(phoneRepository, times(1)).save(any(Phone.class));
    }

    @Test
    void signUpNewUserResourceExceptionThrown() {

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setEmail("fsalaslazcano12345@gmail.com");

        UserRegistered userAlreadyExists = UserRegistered.builder().build();
        when(userRegisteredRepository.getUserRegisteredByEmail(userSignUpRequest.getEmail()))
                .thenReturn(userAlreadyExists);


        assertThrows(ResourceException.class, () -> userService.signUpUser(userSignUpRequest));

        verify(userRegisteredRepository, times(1)).getUserRegisteredByEmail(userSignUpRequest.getEmail());
        verify(userRegisteredRepository, never()).save(any(UserRegistered.class));
        verify(phoneRepository, never()).save(any(Phone.class));
        verify(jwtUtil, never()).generateJwt(any(UserRegistered.class));
    }

    @Test
    void loginUserSuccessfulLogin() {

        LoginBody loginBody = LoginBody.builder()
                .username("fsalaslazcano@gmail.com")
                        .password("cGFzc3dvcmQ=").build();

        UserRegistered userRegistered = UserRegistered.builder()
                .id(UUID.randomUUID())
                .email(loginBody.getUsername())
                .password("password")
                .lastLogin(LocalDateTime.now())
                .created(LocalDateTime.now())
                .name("Francisco Salas Lazcano")
                .isActive(true)
                .build();
        when(userRegisteredRepository.getUserRegisteredByEmailAndPassword(loginBody.getUsername(),
                        new String(Base64.getDecoder().decode(loginBody.getPassword()))))
                .thenReturn(userRegistered);

        List<Phone> phones = new ArrayList<>();
        phones.add(Phone.builder()
                .countryCode("+56")
                .cityCode(2)
                .number(945836647)
                .userRegisteredId(userRegistered.getId())
            .build());
        when(phoneRepository.getPhonesByUserRegisteredId(userRegistered.getId()))
                .thenReturn(phones);

        when(jwtUtil.generateJwt(userRegistered))
                .thenReturn("mocked-jwt-token");

        UserLoginResponse response = userService.loginUser(loginBody);

        assertNotNull(response);
        assertNotNull(response.getLastLogin());
        assertEquals(userRegistered.getCreated(), response.getCreated());
        assertEquals(userRegistered.getIsActive(), response.getIsActive());
        assertEquals(userRegistered.getId(), response.getId());
        assertEquals(userRegistered.getEmail(), response.getEmail());
        assertEquals(userRegistered.getName(), response.getName());
        assertEquals(phones, response.getPhones());
        assertEquals("mocked-jwt-token", response.getToken());

        verify(userRegisteredRepository, times(1))
                .getUserRegisteredByEmailAndPassword(loginBody.getUsername(),
                        new String(Base64.getDecoder().decode(loginBody.getPassword())));
        verify(userRegisteredRepository, times(1)).save(userRegistered);
        verify(phoneRepository, times(1)).getPhonesByUserRegisteredId(userRegistered.getId());
        verify(jwtUtil, times(1)).generateJwt(userRegistered);
    }

    @Test
    void loginUser_InvalidCredentials_UnauthorizedAccessThrown() {

        LoginBody loginBody = LoginBody.builder()
                .username("fsalaslazcano@gmail.com")
                .password("cGFzc3dvcmQ=").build();


        when(userRegisteredRepository.getUserRegisteredByEmailAndPassword(loginBody.getUsername(), "password1"))
                .thenReturn(null);


        assertThrows(UnauthorizedAccessException.class, () -> userService.loginUser(loginBody));

        verify(userRegisteredRepository, times(1))
                .getUserRegisteredByEmailAndPassword(loginBody.getUsername(),
                        new String(Base64.getDecoder().decode(loginBody.getPassword())));
        verify(userRegisteredRepository, never()).save(any(UserRegistered.class));
        verify(phoneRepository, never()).getPhonesByUserRegisteredId(any(UUID.class));
        verify(jwtUtil, never()).generateJwt(any(UserRegistered.class));
    }

}
