package com.sportman.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateStockRequest {

    @NotNull(message = "PRODUCT_STOCK_NOT_EMPTY")
    @Min(value = 0, message = "PRODUCT_STOCK_INVALID")
    Integer stock;

}
