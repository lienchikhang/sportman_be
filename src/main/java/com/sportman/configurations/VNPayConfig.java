package com.sportman.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayConfig {

    @Value("${payment.vnpay.vnp_TmnCode}")
    String vnp_TmnCode;

    @Value("${payment.vnpay.vnp_HashSecret}")
    String vnp_HashSecret;

    @Value("${payment.vnpay.vnp_Url}")
    String vnp_Url;

    @Value("${payment.vnpay.vnp_Version}")
    String vnp_Version;

    @Value("${payment.vnpay.vnp_Command}")
    String vnp_Command;

    @Value("${payment.vnpay.vnp_ReturnUrl}")
    String vnp_ReturnUrl;

    @Value("${payment.vnpay.vnp_OrderInfo}")
    String vnp_OrderInfo;

    @Value("${payment.vnpay.vnp_CurrCode}")
    String vnp_CurrCode;

    public Map<String, String> getVNPAYConfig() {
        Map<String, String> vnpayConfig = new HashMap<>();

        //set transaction info
        vnpayConfig.put("vnp_TmnCode", vnp_TmnCode);
        vnpayConfig.put("vnp_HashSecret", vnp_HashSecret);
        vnpayConfig.put("vnp_Url", vnp_Url);
        vnpayConfig.put("vnp_Version", vnp_Version);
        vnpayConfig.put("vnp_Command", vnp_Command);
        vnpayConfig.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnpayConfig.put("vnp_OrderInfo", vnp_OrderInfo);
        vnpayConfig.put("vnp_Locale", "vn");
        vnpayConfig.put("vnp_CurrCode", vnp_CurrCode);

        return vnpayConfig;
    }
}
