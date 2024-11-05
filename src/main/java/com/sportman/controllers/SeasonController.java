package com.sportman.controllers;

import com.sportman.dto.request.SeasonCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.SeasonResponse;
import com.sportman.dto.response.page.SeasonPageResponse;
import com.sportman.services.interfaces.SeasonService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeasonController {

    SeasonService seasonService;

    @GetMapping
    public ApiResponse<SeasonPageResponse> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<SeasonPageResponse>builder()
                .statusCode(200)
                .content(seasonService.getAll(pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<SeasonResponse> create(@RequestBody @Valid SeasonCreateRequest request) {
        return ApiResponse.<SeasonResponse>builder()
                .statusCode(201)
                .content(seasonService.create(request))
                .build();
    }

    @DeleteMapping("/delete/{seasonId}")
    public ApiResponse<Void> delete(
            @PathVariable("seasonId") String seasonId
    ) {

        String[] year = seasonId.split("-");

        SeasonCreateRequest request = SeasonCreateRequest
                .builder()
                .yearStart(Integer.parseInt(year[0]))
                .yearEnd(Integer.parseInt(year[1]))
                .build();

        seasonService.delete(request);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .build();
    }

}
