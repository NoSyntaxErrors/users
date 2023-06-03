package com.factory.users.service;

import com.factory.users.model.LoginBody;
import com.factory.users.model.UserLoginResponse;
import com.factory.users.model.UserSignUpRequest;
import com.factory.users.model.UserSignUpResponse;

public interface UserService {

    UserSignUpResponse signUpUser(UserSignUpRequest userSignUpRequest);

    UserLoginResponse loginUser(LoginBody loginBody);
}
