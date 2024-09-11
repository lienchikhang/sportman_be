package com.sportman.mappers;

import com.sportman.dto.response.OrderResponse;
import com.sportman.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    public OrderResponse toResponse(Order order);
}
