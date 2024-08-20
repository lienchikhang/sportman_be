package com.sportman.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportman.dto.response.ApiResponse;
import com.sportman.exceptions.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtHandlingEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorCode errorCode = ErrorCode.UN_AUTHENTICATED;

        ApiResponse apiResponse = ApiResponse
                .builder()
                .msg(errorCode.getMsg())
                .statusCode(errorCode.getStatusCode())
                .build();

        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        response.flushBuffer();
    }
}
