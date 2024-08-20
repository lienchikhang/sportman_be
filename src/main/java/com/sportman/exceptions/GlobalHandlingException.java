package com.sportman.exceptions;

import com.sportman.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
@ControllerAdvice
public class GlobalHandlingException {

    //fallback handling exception
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingFallbackException(Exception e) {

        log.warn(e.getMessage());
        log.warn(e.toString());

        ErrorCode errorCode = ErrorCode.FALLBACK_EXC;

        ApiResponse apiResponse = ApiResponse
                .builder()
                .statusCode(errorCode.getCode())
                .msg(errorCode.getMsg())
                .build();

        return ResponseEntity.status(500).body(apiResponse);

    }

    //AppException
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException e) {

        ErrorCode errorCode = e.getErrorCode();

        ApiResponse apiResponse = ApiResponse
                .builder()
                .statusCode(errorCode.getStatusCode())
                .msg(errorCode.getMsg())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    //HttpRequestMethodNotSupportedException
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handlingMethodNotSupportException(HttpRequestMethodNotSupportedException e) {

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        ApiResponse apiResponse = ApiResponse
                .builder()
                .msg(e.getMessage())
                .statusCode(errorCode.getStatusCode())
                .build();

        return ResponseEntity.status(400).body(apiResponse);

    }

    //MethodArgumentNotValidException
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        Map<String, Object> attributes = null;

        //get all error messages
        errorCode = ErrorCode.valueOf(e.getBindingResult().getFieldError().getDefaultMessage());

//
//        try {
//            errorCode = ErrorCode.valueOf(e.getFieldError().getDefaultMessage());
//
//            var constaint = e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
//
//            attributes = constaint.getConstraintDescriptor().getAttributes();
//        } catch (Exception exc) {
//
//        }

        ApiResponse apiResponse = ApiResponse
                .builder()
                .statusCode(errorCode.getStatusCode())
                .msg(errorCode.getMsg())
                .build();

        return ResponseEntity.status(400).body(apiResponse);
    }


}
