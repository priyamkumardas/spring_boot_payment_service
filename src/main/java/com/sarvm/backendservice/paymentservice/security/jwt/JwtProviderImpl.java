package com.sarvm.backendservice.paymentservice.security.jwt;

import com.sarvm.backendservice.paymentservice.security.CustomUserDetails;
import com.sarvm.backendservice.paymentservice.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtProviderImpl implements IJwtProvider{

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProviderImpl.class);

    @Override
    public boolean isTokenValid(HttpServletRequest request){
        String jwtToken = SecurityUtil.extractTokenFromRequest(request);

        if(jwtToken == null){
            LOGGER.info("Token Not Present in Auth header");
            return false;
        }

        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payloadString = new String(decoder.decode(chunks[1]));
        try{
            JSONObject payLoadData = new JSONObject(payloadString);
            JSONArray scopes = (JSONArray) payLoadData.get("scope");
            int length = scopes.length();
            Set<String> scopeSet = new HashSet<>();
            for(int i=0; i<length; i++){
                scopeSet.add((String)scopes.get(i));
            }
            Long expiryTime = (Long) Long.parseLong(payLoadData.get("exp").toString());
            if(expiryTime < System.currentTimeMillis()/1000){
                return false;
            }
            String userId = (String)payLoadData.get("userId");
            if(userId == null) return false;
        }catch (JSONException exc){
            LOGGER.info("Token Not Valid. Payload recieved in Auth Token => "+payloadString);
            return false;
        }
        return true;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request){
        String jwtToken = SecurityUtil.extractTokenFromRequest(request);

        if(jwtToken == null){
            return null;
        }

        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payloadString = new String(decoder.decode(chunks[1]));
        JSONObject payLoadData;
        try {
            payLoadData = new JSONObject(payloadString);
            JSONArray scopes = (JSONArray) payLoadData.get("scope");
            int length = scopes.length();
            Set<String> scopeSet = new HashSet<>();
            for(int i=0; i<length; i++){
                scopeSet.add((String)scopes.get(i));
            }
            String userId = (String)payLoadData.get("userId");
            Set<GrantedAuthority> set = scopeSet.stream().map(SecurityUtil::convertToAuthority)
                    .collect(Collectors.toSet());
            UserDetails userDetails = CustomUserDetails.builder().username(userId).authorities(set).build();
            return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        } catch (JSONException e) {
            LOGGER.info("Token Not Valid. Payload recieved in Auth Token => "+payloadString);
            return null;
        }

    }
}
