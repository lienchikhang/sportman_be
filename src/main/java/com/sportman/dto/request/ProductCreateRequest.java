package com.sportman.dto.request;

import com.sportman.annotations.ValidSeason;
import com.sportman.enums.ProductLeague;
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
public class ProductCreateRequest {

    @NotEmpty(message = "PRODUCT_NAME_NOT_EMPTY")
    String productName;

    @NotEmpty(message = "PRODUCT_DESC_NOT_EMPTY")
    String productDesc;

    @Min(value = 50000, message = "PRODUCT_PRICE_INVALID")
    @Max(value = 10000000, message = "PRODUCT_PRICE_INVALID")
    Integer productPrice;

    @NotEmpty(message = "PRODUCT_SEASON_INVALID")
    @ValidSeason
    List<Integer> season;

    @NotEmpty(message = "CLUB_NAME_NOT_NULL")
    String club;

    @NotEmpty(message = "SIZE_NOT_EMPTY")
    List<SizeAndStock> sizes;

    @NotEmpty(message = "COLOR_NOT_EMPTY")
    List<String> colors;

    Integer stock;

    ProductLeague league;


}
