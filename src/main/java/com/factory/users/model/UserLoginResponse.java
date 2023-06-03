package com.factory.users.model;

import com.factory.users.repositories.entities.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLoginResponse {

    private UUID id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private Boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;

}
