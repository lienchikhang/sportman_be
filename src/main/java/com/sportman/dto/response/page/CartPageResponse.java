package com.sportman.dto.response.page;

import com.sportman.dto.response.CartCreateResponse;
import com.sportman.dto.response.ColorCreateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartPageResponse {
    List<CartCreateResponse> carts;
    Integer currentPage;
    Long totalPage;
    Long totalElements;
}
