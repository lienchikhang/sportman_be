package com.sportman.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClubUpdateRequest {

    @Nullable
    String clubName;

    @Nullable
//    @Min(value = 7, message = "CLUB_COLOR_HEX_OVER_BOUNCE")
    @Length(min = 7, max = 7, message = "CLUB_COLOR_HEX_OVER_BOUNCE")
    String colorHex;

    @Nullable
//    @Min(value = 3, message = "CLUB_COLOR_HEX_OVER_BOUNCE")
    @Length(min = 3, max = 3, message = "CLUB_SHORT_NAME_OVER_BOUNCE")
    String shortName;

}
