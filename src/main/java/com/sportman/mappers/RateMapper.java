package com.sportman.mappers;

import com.sportman.dto.response.RateResponse;
import com.sportman.entities.Rate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RateMapper {

    public RateResponse toResponse(Rate rate);

}
