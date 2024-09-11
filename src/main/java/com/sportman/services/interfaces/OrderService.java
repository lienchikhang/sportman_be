package com.sportman.services.interfaces;

import com.sportman.dto.request.OrderCreateRequest;
import com.sportman.dto.response.OrderResponse;
import com.sportman.dto.response.page.OrderPageResponse;
import com.sportman.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    //user
    public OrderPageResponse getAllByUser(Pageable pageable, String sort);

    public OrderResponse create(OrderCreateRequest request);

    public OrderResponse cancel(String order);


    //admin
    public OrderPageResponse getAll(Pageable pageable, String sort, OrderStatus status);

    //delivery
    public OrderResponse confirmPaid(String orderId);

    public OrderResponse confirmDelivered(String orderId);

}
