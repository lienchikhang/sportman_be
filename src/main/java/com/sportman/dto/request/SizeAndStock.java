package com.sportman.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeAndStock {
    @NotEmpty(message = "PRODUCT_SIZE_EMPTY")
    String size;

    @NotEmpty(message = "PRODUCT_STOCK_EMPTY")
    @Min(value = 1, message = "PRODUCT_STOCK_INVALID")
    @Max(value = 100, message = "PRODUCT_STOCK_INVALID")
    Integer stock;
}
