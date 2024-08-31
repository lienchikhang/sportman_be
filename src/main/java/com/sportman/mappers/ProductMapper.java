package com.sportman.mappers;

import com.sportman.dto.request.ProductCreateRequest;
import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.ProductGetResponse;
import com.sportman.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "frontImage", ignore = true),
            @Mapping(target = "backImage", ignore = true),
            @Mapping(target = "season", ignore = true),
            @Mapping(target = "club", ignore = true),
            @Mapping(target = "stocks", ignore = true),
            @Mapping(target = "colors", ignore = true)
    })
    public Product toProduct(ProductCreateRequest request);

    @Mappings({
            @Mapping(target = "stocks", ignore = true)
    })
    public ProductCreateResponse toResponse(Product product);


    @Mappings({
            @Mapping(target = "colors", ignore = true),
            @Mapping(target = "id", source = "id")
    })
    public ProductGetResponse toGetResponse(Product product);
}
