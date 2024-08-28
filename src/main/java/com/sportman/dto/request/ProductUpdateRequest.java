package com.sportman.dto.request;

import com.sportman.annotations.ValidSeason;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    String productName;

    String productDesc;

    @Min(value = 50000, message = "PRODUCT_PRICE_INVALID")
    @Max(value = 10000000, message = "PRODUCT_PRICE_INVALID")
    Integer productPrice;

    @Length(min = 2, max = 2, message = "PRODUCT_SEASON_INVALID")
    @ValidSeason
    List<Integer> season;

    String club;

    String sizeTag;

    String colorHex;

    Integer stock;

}