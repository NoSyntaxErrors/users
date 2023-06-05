package com.factory.users.converter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.factory.users.model.LoginBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtConverterTest {

    private JwtConverter jwtConverter;

    @BeforeEach
    void setUp() {
        jwtConverter = new JwtConverter();
    }

    @Test
    void testConvert() {

        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODU5NDczODYsImlhdCI6MTY4NTk0Njc4NiwiaXNzIjoiZnNhbGFzbGF6Y2Fub0BnbWFpbC5jb20iLCJhdWQiOiJmc2FsYXNsYXpjYW5vQGdtYWlsLmNvbSIsIm5hbWUiOiJGcmFuY2lzY28gU2FsYXMgTGF6Y2FubyIsInA0c3N3b3JkIjoiUm5KaGJtTnBjMk52T1RVPSIsInV1aWQiOiJkN2RmZGZjNC1kZjFiLTRjZWQtODVmMi0wMjcwMzA2MWUyOTMifQ.Seg9gIKyKKU-wxgmNfzFcuN-ITFI_pjhRo5WTUq7Cas";

        LoginBody result = jwtConverter.convert(token);

        assertNotNull(result);
        assertEquals("fsalaslazcano@gmail.com", result.getUsername());
        assertEquals("RnJhbmNpc2NvOTU=", result.getPassword());
    }
}
