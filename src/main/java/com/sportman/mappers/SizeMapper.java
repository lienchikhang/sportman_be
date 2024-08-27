package com.sportman.mappers;

import com.sportman.dto.request.SizeCreateRequest;
import com.sportman.dto.response.SizeCreateResponse;
import com.sportman.entities.Size;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SizeMapper {

    public Size toSize(SizeCreateRequest request);

    public SizeCreateResponse toSizeResponse(Size size);

}
