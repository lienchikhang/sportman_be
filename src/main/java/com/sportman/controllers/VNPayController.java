package com.sportman.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sportman.dto.request.OrderCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.VNPayResponse;
import com.sportman.dto.response.VNPayResultResponse;
import com.sportman.services.interfaces.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/vnpay")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VNPayController {

    VNPAYService vnpayService;

    @GetMapping("/create-payment-url")
    public ApiResponse<VNPayResponse> createPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        return ApiResponse.<VNPayResponse>builder()
                .content(vnpayService.createPaymentUrl(request))
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<VNPayResultResponse> callbackUrl(HttpServletRequest request) throws UnsupportedEncodingException, JsonProcessingException {
        return ApiResponse.<VNPayResultResponse>builder()
                .content(vnpayService.callbackUrl(request))
                .build();
    }

    @PostMapping("/get-json-data")
    public ApiResponse<String> getJsonData(@RequestBody OrderCreateRequest request) {
        return ApiResponse.<String>builder()
                .content(vnpayService.createJSONData(request))
                .build();
    }

}
