package com.sportman.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateResponse {

    String username;
    String userAvatar;
    String rateComment;
    LocalDate createdAt;

}
