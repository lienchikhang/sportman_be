package com.sportman.mappers;

import com.sportman.dto.request.RateCreateRequest;
import com.sportman.dto.response.RateCreateResponse;
import com.sportman.dto.response.RateResponse;
import com.sportman.entities.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RateMapper {

    @Mappings({
            @Mapping(target = "user", ignore = true)
    })
    public RateResponse toResponse(Rate rate);

    @Mappings({
            @Mapping(target = "user", ignore = true)
    })
    public RateCreateResponse toCreateResponse(Rate rate);

    @Mappings({
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "id", ignore = true),
    })
    public Rate toRate(RateCreateRequest request);

}
