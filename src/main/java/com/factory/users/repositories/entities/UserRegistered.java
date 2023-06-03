package com.factory.users.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_registered")
public class UserRegistered implements Serializable {

    private static final long serialVersionUID = -1895155952222550301L;

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "created", columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    @Column(name = "last_login", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;


}
