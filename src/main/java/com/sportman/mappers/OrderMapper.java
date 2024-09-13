package com.sportman.mappers;

import com.sportman.dto.response.OrderResponse;
import com.sportman.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    public OrderResponse toResponse(Order order);
}
