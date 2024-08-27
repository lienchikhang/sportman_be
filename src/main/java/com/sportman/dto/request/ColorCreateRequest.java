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
public class ColorCreateRequest {

    @NotEmpty(message = "COLOR_NOT_EMPTY")
    @Length(min = 7, max = 7, message = "COLOR_OVER_BOUNCE")
    String colorHex;

}
