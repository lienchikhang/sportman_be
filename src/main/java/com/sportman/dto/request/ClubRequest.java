package com.sportman.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClubRequest {

    @NotNull(message = "CLUB_NAME_NOT_NULL")
    String clubName;

    @NotNull(message = "CLUB_COLOR_HEX_NOT_NULL")
//    @Min(value = 7, message = "CLUB_COLOR_HEX_OVER_BOUNCE")
    @Length(min = 7, max = 7, message = "CLUB_COLOR_HEX_OVER_BOUNCE")
    String colorHex;

    @NotNull(message = "CLUB_SHORT_NAME_NOT_NULL")
//    @Min(value = 3, message = "CLUB_COLOR_HEX_OVER_BOUNCE")
    @Length(min = 3, max = 3, message = "CLUB_SHORT_NAME_OVER_BOUNCE")
    String shortName;

    private Boolean isDeleted = false;

}
