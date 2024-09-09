package com.sportman.mappers;

import com.sportman.dto.response.CartCreateResponse;
import com.sportman.entities.CartDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartDetailMapper {

    public CartCreateResponse toResponse(CartDetail cartDetail);

}
