package com.factory.users.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginBody {

    private String username;
    private String password;
}
