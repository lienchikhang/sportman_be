package com.sportman.controllers;

import com.sportman.dto.request.SeasonCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.SeasonResponse;
import com.sportman.services.interfaces.SeasonService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeasonController {

    SeasonService seasonService;

    @GetMapping
    public ApiResponse<List<SeasonResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int pageSize
    ) {
        return ApiResponse.<List<SeasonResponse>>builder()
                .content(seasonService.getAll(page, pageSize))
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
