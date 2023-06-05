package com.factory.users.security;

import com.factory.users.service.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenFilterTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenFilter, "jwtSecretKey", "ZnJhbmNpc2NvYW5kcmVzc2FsYXNiYXJyYWxlcw==");
    }

    @Test
    void doFilterInternalValidJwtToken() throws ServletException, IOException {

        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiRnJhbmNpc2NvIFNhbGFzIExhemNhbm8iLCJ1dWlkIjoiNzFjMDE0YTYtM2RlNS00OWFlLThlZWItYmIyYjcxOWYxMzQ0IiwicDRzc3dvcmQiOiJSbkpoYm1OcGMyTnZPVFU9IiwiaWF0IjoxNjg1OTQxOTU0LCJpc3MiOiIxZnNhbGFzbGF6Y2Fub0BnbWFpbC5jb20iLCJhdWQiOiIxZnNhbGFzbGF6Y2Fub0BnbWFpbC5jb20iLCJleHAiOjE2ODU5NDI1NTR9.gq5T-gVhPZ8EX1y6mECOiKEeu31QyMcZNmVsXl8f_e4";
        String header = "Bearer " + jwtToken;
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        User userDetails = new User("fsalaslazcano@gmail.com", "", Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(header);
        when(jwtTokenUtil.getUserFromJwt(jwtToken)).thenReturn(userDetails);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        Authentication result = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(result);
        assertEquals(userDetails, result.getPrincipal());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalInvalidJwtToken() throws ServletException, IOException {

       String header = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODU5Mj6MTY4NTkzODMwNCwiaXNzIjoiZnNhbGFzbGF6Y2Fub0BnbWFpbC5jb20iLCJhdWQiOiJmc2FsYXNsYXpjYW5vQGdtYWlsLmNvbSIsIm5hbWUiOiJGcmFuY2lzY28gU2FsYXMgTGF6Y2FubyIsInA0c3N3b3JkIjoiUm5KaGJtTnBjMk52T1RVPSIsInV1aWQiOiI1MDUwMmJiNy02OTg1LTRkMmUtOTdlYy03ZGI1NjAwMjcwOGIifQ.XvUQCWarDNlUlaYFLsLTNthXmzSlkf0boLfl5hEYArI";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(header);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalNullJwtToken() throws ServletException, IOException {

        String header = "";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(header);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalRefreshesToken() throws ServletException, IOException {

        String expiredJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiRnJhbmNpc2NvIFNhbGFzIExhemNhbm8iLCJ1dWlkIjoiZjM3YWNkZmYtMGRjZi00YTJkLWJmZTAtNDEwYzUwNjgxN2EzIiwicDRzc3dvcmQiOiJSbkpoYm1OcGMyTnZPVFU9IiwiaWF0IjoxNjg1ODI0NzM0LCJpc3MiOiIyZnNhbGFzbGF6Y2Fub0BnbWFpbC5jb20iLCJhdWQiOiIyZnNhbGFzbGF6Y2Fub0BnbWFpbC5jb20iLCJleHAiOjE2ODU4MjUzMzR9.Rrna83LaN9O7zagVN3tU_jkZ7Y9r8teVLyr82A4Uyng";
        String header = "Bearer " + expiredJwtToken;
        String refreshedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODU5NDA0MjYsImlhdCI6MTY4NTkzOTgyNiwiaXNzIjoiMmZzYWxhc2xhemNhbm9AZ21haWwuY29tIiwiYXVkIjoiMmZzYWxhc2xhemNhbm9AZ21haWwuY29tIiwibmFtZSI6IkZyYW5jaXNjbyBTYWxhcyBMYXpjYW5vIiwicDRzc3dvcmQiOiJSbkpoYm1OcGMyTnZPVFU9IiwidXVpZCI6ImFiODZjMDhiLTEzZmMtNDI4ZC04YTMwLWVkOGRkZTI0MWY0MiJ9.mCUZXt8gkiBhlyGpG7hDVd9ILWm5awcLH5fH5qPvIIQ";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(header);
        when(response.getHeader("RefreshToken")).thenReturn(refreshedToken);
        doThrow(ExpiredJwtException.class).when(jwtTokenUtil).getUserFromJwt(expiredJwtToken);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setHeader(eq("RefreshToken"), anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
