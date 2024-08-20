package com.sportman.dto.request;

import com.sportman.annotations.IsAfterStartYear;
import com.sportman.annotations.IsValidYear;
import com.sportman.annotations.NonZero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeasonCreateRequest {

    @NonZero
    @Min(value = 1970, message = "SEASON_YEAR_INVALID")
//    @IsValidYear
    int yearStart;

//    @IsValidYear
    @NonZero
    @Min(value = 1970, message = "SEASON_YEAR_INVALID")
    int yearEnd;

    boolean isDeleted = false;
}
