package com.factory.users.security;

import org.springframework.security.core.userdetails.User;

public interface UserLoginDetailService {

    User loadUsername(String email);
}
