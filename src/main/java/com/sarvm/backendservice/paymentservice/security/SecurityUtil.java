package com.sarvm.backendservice.paymentservice.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtil {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String AUTH_HEADER = "authorization";
    private static final String AUTH_TYPE = "Bearer";
    private static final String AUTH_PREFIX = AUTH_TYPE+" ";

    public static SimpleGrantedAuthority convertToAuthority(String role){
        String formattedRole = role.startsWith(ROLE_PREFIX)? role: ROLE_PREFIX+role;
        return new SimpleGrantedAuthority(formattedRole);
    }

    public static String extractTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTH_HEADER);
        if(StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }
}
