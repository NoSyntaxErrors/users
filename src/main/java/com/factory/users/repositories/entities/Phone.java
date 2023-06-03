package com.factory.users.repositories.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 826859220435923511L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "city_code")
    private Integer cityCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "user_registered_id")
    private UUID userRegisteredId;

}