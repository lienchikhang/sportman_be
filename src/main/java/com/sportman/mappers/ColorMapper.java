package com.sportman.mappers;

import com.sportman.dto.request.ColorCreateRequest;
import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.entities.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    public Color toColor(ColorCreateRequest request);

    public ColorCreateResponse toColorResponse(Color color);

}
