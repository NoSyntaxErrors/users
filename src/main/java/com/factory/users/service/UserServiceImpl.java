package com.factory.users.service;

import com.factory.users.model.LoginBody;
import com.factory.users.model.UserLoginResponse;
import com.factory.users.model.UserSignUpRequest;
import com.factory.users.model.UserSignUpResponse;
import com.factory.users.model.exception.ResourceException;
import com.factory.users.repositories.PhoneRepository;
import com.factory.users.repositories.UserRegisteredRepository;
import com.factory.users.repositories.entities.Phone;
import com.factory.users.repositories.entities.UserRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRegisteredRepository userRegisteredRepository;
    private final PhoneRepository phoneRepository;
    private final JwtTokenUtil jwtUtil;

    @Override
    public UserSignUpResponse signUpUser(UserSignUpRequest userSignUpRequest) {

        UserRegistered userAlreadyExists = userRegisteredRepository.getUserRegisteredByEmail(userSignUpRequest.getEmail());


        if (userAlreadyExists != null) {
            throw new ResourceException();

        } else {
            UUID uuid = UUID.randomUUID();

            UserRegistered newUserRegistered = UserRegistered.builder()
                    .id(uuid)
                    .email(userSignUpRequest.getEmail())
                    .password(userSignUpRequest.getPassword())
                    .name(userSignUpRequest.getName())
                    .created(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isActive(true)
                    .build();

            newUserRegistered.setToken(getTokenJwt(newUserRegistered));
            newUserRegistered = userRegisteredRepository.save(newUserRegistered);

            userSignUpRequest.getPhones().forEach(phoneReq -> phoneRepository.save(Phone.builder()
                    .userRegisteredId(uuid)
                    .cityCode(phoneReq.getCitycode())
                    .countryCode(phoneReq.getCountrycode())
                    .number(phoneReq.getNumber()).build()));


            return UserSignUpResponse.builder()
                    .lastLogin(newUserRegistered.getLastLogin()).created(newUserRegistered.getCreated())
                    .isActive(newUserRegistered.getIsActive()).id(newUserRegistered.getId())
                    .token(newUserRegistered.getToken())
                    .build();

        }
    }


    @Override
    public UserLoginResponse loginUser(LoginBody loginBody) {

        UserRegistered userRegistered = userRegisteredRepository.getUserRegisteredByEmailAndPassword(loginBody.getUsername(),
                new String(Base64.getDecoder().decode(loginBody.getPassword())));
        List<Phone> phones = phoneRepository.getPhonesByUserRegisteredId(userRegistered.getId());


        UserLoginResponse userLoginResponse = UserLoginResponse.builder()
                .lastLogin(userRegistered.getLastLogin())
                .created(userRegistered.getCreated())
                .isActive(userRegistered.getIsActive())
                .id(userRegistered.getId())
                .email(userRegistered.getEmail())
                .name(userRegistered.getName())
                .phones(phones)
                .token(getTokenJwt(userRegistered))
                .build();

        userRegistered.setLastLogin(LocalDateTime.now());
        userRegisteredRepository.save(userRegistered);

        return userLoginResponse;

    }

    private String getTokenJwt(UserRegistered userRegistered) {
        return jwtUtil.generateJwt(userRegistered);
    }

}
