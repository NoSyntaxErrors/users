package com.factory.users.api;

import com.factory.users.model.LoginBody;
import com.factory.users.model.UserLoginResponse;
import com.factory.users.model.UserSignUpRequest;
import com.factory.users.model.UserSignUpResponse;
import com.factory.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> createUser(
            @RequestBody @Valid UserSignUpRequest userSignUpRequest) {

        UserSignUpResponse userSignUpResponse = userService.signUpUser(userSignUpRequest);

        return new ResponseEntity<>(userSignUpResponse, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestHeader("Authorization") LoginBody loginBody) {

        UserLoginResponse userLoginResponse = userService.loginUser(loginBody);

        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }

}
