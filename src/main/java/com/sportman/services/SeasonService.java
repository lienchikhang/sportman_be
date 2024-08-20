package com.sportman.services;

import com.sportman.dto.request.SeasonCreateRequest;
import com.sportman.dto.response.SeasonResponse;
import com.sportman.entities.Season;
import com.sportman.entities.SeasonId;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.SeasonMapper;
import com.sportman.repositories.SeasonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeasonService {

    SeasonRepository seasonRepository;
    SeasonMapper seasonMapper;

    /**
     * used to get all season list with pagination
     * @param page
     * @param pageSize
     * @return List<SeasonResponse>
     */
    public List<SeasonResponse> getAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return seasonRepository.findAllByIsDeletedFalse(pageable)
                .stream()
                .map(seasonMapper::toSeasonResponse).toList();
    }

    /**
     * used to create a season
     * @param  request
     * @return SeasonResponse
     */
    public SeasonResponse create(SeasonCreateRequest request) {

        //check yearStart & yearEnd
        if (request.getYearStart() > request.getYearEnd()) throw new AppException(ErrorCode.SEASON_YEAR_INVALID);

        //check gap between yearStart & yearEnd equals 1 or not
        if (request.getYearEnd() - request.getYearStart() != 1) throw new AppException(ErrorCode.SEASON_YEAR_INVALID);

        //check existed season
        if (seasonRepository.existsById(seasonMapper.toSeasonId(request))) throw new AppException(ErrorCode.SEASON_EXISTED);

        //create
        SeasonId seasonId = seasonMapper.toSeasonId(request);
        Season newSeason = seasonMapper.toSeason(seasonId);
        newSeason.setIsDeleted(request.isDeleted());

        return seasonMapper.toSeasonResponse(
                seasonRepository.save(newSeason)
        );
    }

    /**
     * used to delete an existed season
     * @param request
     * @return
     */
    public void delete(SeasonCreateRequest request) {
        //check existed season
        Season season = seasonRepository.findById(seasonMapper.toSeasonId(request)).orElseThrow(() -> new AppException(ErrorCode.SEASON_NOT_FOUND));

        //delete
        season.setIsDeleted(true);

        seasonRepository.save(season);

    }
}
