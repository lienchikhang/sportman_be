package com.sportman.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeCreateRequest {

    @NotEmpty(message = "SIZE_NOT_EMPTY")
    @Length(max = 10, message = "SIZE_OVER_LENGTH")
    String sizeTag;

}
