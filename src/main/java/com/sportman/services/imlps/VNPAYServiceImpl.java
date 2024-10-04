package com.sportman.services.imlps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sportman.configurations.VNPayConfig;
import com.sportman.dto.request.OrderCreateRequest;
import com.sportman.dto.response.VNPayResponse;
import com.sportman.dto.response.VNPayResultResponse;
import com.sportman.entities.*;
import com.sportman.enums.OrderStatus;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.repositories.OrderRepository;
import com.sportman.repositories.ProductRepository;
import com.sportman.repositories.SizeRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.services.interfaces.VNPAYService;
import com.sportman.utils.VnpayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VNPAYServiceImpl implements VNPAYService {

    VNPayConfig vnPayConfig;
    OrderRepository orderRepository;
    ProductRepository productRepository;
    SizeRepository sizeRepository;
    UserRepository userRepository;

    @Override
    @PreAuthorize("hasRole('USER') || hasAuthority('MAKE_PAYMENT')")
    public VNPayResponse createPaymentUrl(HttpServletRequest req) throws UnsupportedEncodingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String vnp_Version = vnPayConfig.getVnp_Version();
        String vnp_Command = vnPayConfig.getVnp_Command();
        String vnp_OrderInfo = req.getParameter("orders");
        String orderType = "100000";
        String vnp_TxnRef = user.getId();
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();

        int amount = Integer.parseInt(req.getParameter("amount")) * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        String bank_code = req.getParameter("bankCode");
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayUtils.hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getVnp_Url() + "?" + queryUrl;

        return VNPayResponse.builder().paymentUrl(paymentUrl).build();
    }

    @Override
    @Transactional
    public VNPayResultResponse callbackUrl(HttpServletRequest request) throws UnsupportedEncodingException, JsonProcessingException {

//        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> vnp_Params = new HashMap<>();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                vnp_Params.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = vnp_Params.get("vnp_SecureHash");
        vnp_Params.remove("vnp_SecureHash");
        String hashData = VnpayUtils.hashAllFields(vnp_Params);

        log.warn(VnpayUtils.hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData));
        log.warn(vnp_SecureHash);

        if (VnpayUtils.hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData).equals(vnp_SecureHash)) {
            // Kiểm tra kết quả thanh toán hợp lệ
            String responseCode = vnp_Params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                List<Map<String, String>> orders = getJSONData(vnp_Params.get("vnp_OrderInfo")); //co the loi

                //get user
                User user = userRepository.findById(vnp_Params.get("vnp_TxnRef")).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                //create order
                Order newOrder = Order.builder()
                        .status(OrderStatus.PAID)
                        .user(user)
                        .build();

                List<OrderDetail> orderDetails = new ArrayList<>();
                orders.forEach(or -> {

                    Product product = productRepository.findById(or.get("productId"))
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                    orderDetails.add(OrderDetail.builder()
                            .product(product)
                            .price(product.getProductPrice())
                            .amount(Integer.parseInt(or.get("amount")))
                            .size(sizeRepository.findById(or.get("sizeTag"))
                                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND)))
                            .build());
                });

                newOrder.setOrderDetails(orderDetails);
                newOrder.getOrderDetails().forEach(orderDetail -> {
                    orderDetail.setOrder(newOrder);
                    orderDetail.setId(OrderDetailId.builder()
                            .sizeTag(orderDetail.getSize().getSizeTag())
                            .productId(orderDetail.getProduct().getId())
                            .build());
                });

                //save order
                orderRepository.save(newOrder);

                return VNPayResultResponse.builder()
                        .msg("Thanh toán thành công!")
                        .status(responseCode)
                        .build();

            } else {
                return VNPayResultResponse.builder()
                        .msg("Thanh toán thất bại!")
                        .status(responseCode)
                        .build();
            }
        } else {
            return VNPayResultResponse.builder()
                    .status(vnp_Params.get("vnp_ResponseCode"))
                    .msg("Chữ ký không hợp lệ!")
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAuthority('MAKE_PAYMENT')")
    public String createJSONData(OrderCreateRequest request) {

        List<Map<String, String>> orders = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        request.getOrders().forEach(order -> {

            //check product
            Product product = productRepository.findById(order.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            //convert to map object
            Map<String, String> mapOrder = new HashMap<>();
            mapOrder.put("productId", product.getId());
            mapOrder.put("sizeTag",sizeRepository.findById(order.getSizeTag())
                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND)).getSizeTag());
            mapOrder.put("amount", order.getAmount().toString());
            mapOrder.put("price", product.getProductPrice().toString());

            orders.add(mapOrder);
        });

        try {

            //create jsonString
            String jsonString = objectMapper.writeValueAsString(orders);

            //endcode
            String encodeJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.toString());
            return encodeJson;
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Map<String, String>> getJSONData(String jsonData) throws UnsupportedEncodingException, JsonProcessingException, JsonMappingException {

        log.warn("1");
        String decodedJson = URLDecoder.decode(jsonData, StandardCharsets.UTF_8.toString());

        ObjectMapper objectMapper = new ObjectMapper();

        List<Map<String, String>> orders = objectMapper.readValue(decodedJson, new TypeReference<List<Map<String, String>>>() {});
        log.warn("2");

        return orders;
    }
}
