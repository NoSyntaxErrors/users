package com.factory.users.service;

import org.springframework.security.core.userdetails.User;

public interface UserLoginDetailService {

    User loadUsername(String email);
}
