package com.sportman.dto.response.page;

import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.ProductGetResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPageResponse {
    List<ProductGetResponse> products;
    Integer currentPage;
    Long totalPage;
    Long totalElements;
}
