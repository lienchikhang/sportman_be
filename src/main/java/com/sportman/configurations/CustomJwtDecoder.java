package com.sportman.configurations;

import com.sportman.dto.request.AuthIntrospectRequest;
import com.sportman.dto.response.AuthIntrospectResponse;
import com.sportman.services.interfaces.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtDecoder implements JwtDecoder {

    AuthService authService;

    @NonFinal
    @Value("${jwt.secretkey}")
    String secretKey;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            AuthIntrospectResponse res = authService.introspectToken(token);
            if (!res.isAuth()) throw new JwtException("Token is invalid");
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(),"HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build()
                .decode(token);
    }


}
