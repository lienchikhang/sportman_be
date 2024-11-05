package com.sportman.dto.response.page;

import com.sportman.dto.response.SeasonResponse;
import com.sportman.dto.response.SizeCreateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeasonPageResponse {
    List<SeasonResponse> seasons;
    Integer currentPage;
    Long totalPage;
    Long totalElements;
}
