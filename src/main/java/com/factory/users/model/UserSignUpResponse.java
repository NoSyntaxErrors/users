package com.factory.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserResponse
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSignUpResponse {

    private UUID id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private Boolean isActive;

}
