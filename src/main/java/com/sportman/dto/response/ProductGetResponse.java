package com.sportman.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductGetResponse {

    String id;
    String productName;
    Integer productPrice;
    String frontImage;
    String backImage;
    List<String> colors;

}
