package com.factory.users.configuration.converter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.factory.users.model.LoginBody;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class JwtConverter implements Converter<String, LoginBody> {

    @Override
    public LoginBody convert(String source) {


        DecodedJWT jwt = JWT.decode(source.split("Bearer ")[1].trim());

        return LoginBody.builder().username(jwt.getAudience().get(0)).password(jwt.getClaim("p4ssword").asString()).build();
    }
}
