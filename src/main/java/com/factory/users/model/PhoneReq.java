package com.factory.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PhoneReq {

    private Integer number;
    private Integer citycode;
    private String countrycode;

}
