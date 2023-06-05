package com.factory.users.controller;

import com.factory.users.api.UserController;
import com.factory.users.model.*;
import com.factory.users.service.UserService;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createUserTest() throws Exception {
        UserSignUpRequest request = UserSignUpRequest.builder()
                .name("Francisco Salas Lazcano")
                .email("fsalaslazcano@gmail.com")
                .password("Francisco95")
                .phones(List.of(PhoneReq.builder()
                        .number(945836647)
                        .citycode(2)
                        .countrycode("+56").build()))
                .build();

        UserSignUpResponse response = UserSignUpResponse.builder()
                .id(UUID.fromString("17a388e1-17c1-4dbc-9ed9-9679b834f630"))
                .lastLogin(LocalDateTime.now())
                .created(LocalDateTime.now())
                .isActive(true)
            .build();

        when(userService.signUpUser(any(UserSignUpRequest.class))).thenReturn(response);

        mockMvc.perform(
                        post("/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("17a388e1-17c1-4dbc-9ed9-9679b834f630"));

    }

    @Test
    void loginUserTest() throws Exception {

        UserLoginResponse response = UserLoginResponse.builder()
                .email("fsalaslazcano@gmail.com")
                .id(UUID.fromString("17a388e1-17c1-4dbc-9ed9-9679b834f630"))
                .lastLogin(LocalDateTime.now()).build();

        when(userService.loginUser(any(LoginBody.class))).thenReturn(response);

        mockMvc.perform(
                        get("/login")
                                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiRnJhbmNpc2NvIFNhbGFzIExhemNhbm8iLCJ1dWlkIjoiYWU3OGUyODAtODVlNi00N2NmLTk3YmYtZDAxZGYxNTBmNDM4IiwicDRzc3dvcmQiOiJSbkpoYm1OcGMyTnZPVFU9IiwiaWF0IjoxNjg1ODQ4NTI2LCJpc3MiOiJmc2FsYXNsYXpjYW5vQGdtYWlsLmNvbSIsImF1ZCI6ImZzYWxhc2xhemNhbm9AZ21haWwuY29tIiwiZXhwIjoxNjg2OTQ5MTI2fQ.L_GE5-ReEJ_6B8E_P8m8-9TxzMzzxlPSKPnlSc_d-b8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("fsalaslazcano@gmail.com"))
                .andExpect(jsonPath("$.id").value("17a388e1-17c1-4dbc-9ed9-9679b834f630"));
    }
}
