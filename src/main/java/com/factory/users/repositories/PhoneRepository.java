package com.factory.users.repositories;

import com.factory.users.repositories.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {


    List<Phone> getPhonesByUserRegisteredId(UUID uuid);

}
