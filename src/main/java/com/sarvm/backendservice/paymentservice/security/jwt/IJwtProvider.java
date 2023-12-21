package com.sarvm.backendservice.paymentservice.security.jwt;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IJwtProvider {
    public Authentication getAuthentication(HttpServletRequest request);
    boolean isTokenValid(HttpServletRequest request);
}
