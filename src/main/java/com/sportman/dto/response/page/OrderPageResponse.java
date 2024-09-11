package com.sportman.dto.response.page;


import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.dto.response.OrderResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPageResponse {
    List<OrderResponse> orders;
    Integer currentPage;
    Long totalPage;
    Long totalElements;
}
