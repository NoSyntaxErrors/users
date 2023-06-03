package com.factory.users.repositories;

import com.factory.users.repositories.entities.UserRegistered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserRegisteredRepository extends JpaRepository<UserRegistered, UUID> {

    UserRegistered getUserRegisteredByEmail(String email);

    UserRegistered getUserRegisteredByEmailAndPassword(String email, String password);

    @Modifying
    @Query(value = "update user_registered set last_login = :lastLogin where email = :email", nativeQuery = true)
    void updateUserRegisteredByEmailSetLasLogin(LocalDateTime lastLogin, String email);

}
