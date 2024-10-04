package com.sportman.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sportman.dto.request.OrderCreateRequest;
import com.sportman.dto.response.VNPayResponse;
import com.sportman.dto.response.VNPayResultResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface VNPAYService {

    public VNPayResponse createPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException;

    public VNPayResultResponse callbackUrl(HttpServletRequest request) throws UnsupportedEncodingException, JsonProcessingException;

    public String createJSONData(OrderCreateRequest request);

    public List<Map<String, String>> getJSONData(String jsonData) throws UnsupportedEncodingException, JsonProcessingException, JsonMappingException;
}
