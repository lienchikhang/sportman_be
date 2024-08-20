package com.sportman.controllers;

import com.sportman.dto.request.ClubRequest;
import com.sportman.dto.request.ClubUpdateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.ClubResponse;
import com.sportman.entities.Club;
import com.sportman.services.ClubService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClubController {

    ClubService clubService;

    @GetMapping
    public ApiResponse<List<ClubResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page, pageSize);

        return ApiResponse.<List<ClubResponse>>builder()
                .statusCode(200)
                .content(clubService.getAll(pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<ClubResponse> create(@RequestBody @Valid ClubRequest request) {
        return ApiResponse.<ClubResponse>builder()
                .statusCode(201)
                .content(clubService.create(request))
                .build();
    }

    @DeleteMapping("/delete/{clubId}")
    public ApiResponse<ClubResponse> delete(@PathVariable("clubId") String clubId) {
        return ApiResponse.<ClubResponse>builder()
                .statusCode(201)
                .content(clubService.delete(clubId))
                .build();
    }

    @PatchMapping("/update/{clubId}")
    public ApiResponse<Void> update(
            @PathVariable("clubId") String clubId,
            @RequestBody @Valid ClubUpdateRequest request
            ) {
        clubService.update(clubId, request);
        return ApiResponse.<Void>builder()
                .statusCode(200)
                .msg("update successfully")
                .build();

    }
}
