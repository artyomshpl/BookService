package com.shep.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenValidator {
    private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);

    public static Boolean tokenValidCheck(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwt);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        "http://auth-service:8081/auth/validate-token",
                        HttpMethod.POST,
                        entity,
                        String.class
                );
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return true;
                } else {
                    logger.warn("Token validation failed with status code: {}", responseEntity.getStatusCode());
                    return false;
                }
            } catch (RestClientException e) {
                logger.error("Error validating token: {}", e.getMessage());
                return false;
            }
        } else {
            logger.warn("Invalid token format: {}", token);
            return false;
        }
    }
}
