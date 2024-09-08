package com.sportman.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateCreateRequest {

    @NotEmpty(message = "PRODUCT_ID_NOT_EMPTY")
    String productId;

    @Min(value = 1, message = "RATE_NUMBER_INVALID")
    @Max(value = 5, message = "RATE_NUMBER_INVALID")
    Integer rateStar;

    @NotEmpty
    String rateComment;

}
