package com.factory.users.service;

import com.factory.users.model.exception.UnauthorizedAccessException;
import com.factory.users.repositories.UserRegisteredRepository;
import com.factory.users.repositories.entities.UserRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class UserLoginDetailServiceImpl implements UserLoginDetailService {

    private final UserRegisteredRepository userRegisteredRepository;

    @Override
    public User loadUsername(String email) {

        UserRegistered userRegistered = userRegisteredRepository.getUserRegisteredByEmail(email);

        if (userRegistered == null) {
            throw new UnauthorizedAccessException("Usuario y/o contraseña invalidos");
        } else {
            return new User(userRegistered.getEmail(), userRegistered.getPassword(), Collections.emptyList());
        }
    }
}
