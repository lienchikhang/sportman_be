package com.sportman.dto.response.page;

import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.dto.response.RateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatePageResponse {
    List<RateResponse> rates;
    Integer currentPage;
    Long totalPage;
    Long totalElements;
    double sumRate;
}
