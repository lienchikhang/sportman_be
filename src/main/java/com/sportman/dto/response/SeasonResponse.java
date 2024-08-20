package com.sportman.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeasonResponse {
    int yearStart;
    int yearEnd;
}
