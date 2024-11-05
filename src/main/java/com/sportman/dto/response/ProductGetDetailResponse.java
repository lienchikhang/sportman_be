package com.sportman.dto.response;

import com.sportman.entities.ProductSize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductGetDetailResponse {

    String id;
    String productName;
    Integer productPrice;
    String frontImage;
    String backImage;
    List<String> colors;
    List<Stocks> stocks;
    List<Integer> seasons;
    String club;
}
