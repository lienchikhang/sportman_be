package com.sportman.services.imlps;

import com.sportman.dto.request.ClubRequest;
import com.sportman.dto.request.ClubUpdateRequest;
import com.sportman.dto.response.ClubResponse;
import com.sportman.entities.Club;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.ClubMapper;
import com.sportman.repositories.ClubRepository;
import com.sportman.services.interfaces.ClubService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClubServiceImpl implements ClubService {

    ClubRepository clubRepository;
    ClubMapper clubMapper;

    @Override
    public List<ClubResponse> getAll(Pageable pageable) {
        //get clubs
        return clubRepository
                .findAllByIsDeletedFalse(pageable)
                .stream()
                .map(clubMapper::toClubResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ClubResponse create(ClubRequest request) {
        //check exist
        boolean isExisted = clubRepository.existsById(request.getClubName());
        if (isExisted) throw new AppException(ErrorCode.CLUB_EXISTED);

        //create new club
        request.setClubName(request.getClubName().toUpperCase());
        request.setShortName(request.getShortName().toUpperCase());
        request.setColorHex(request.getColorHex().toUpperCase());
        return clubMapper.toClubResponse(clubRepository.save(clubMapper.toClub(request)));
    }

    @Override
    public ClubResponse delete(String clubId) {
        //check exist
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new AppException(ErrorCode.CLUB_NOT_FOUND));

        //delete
        club.setIsDeleted(true);

        return clubMapper.toClubResponse(clubRepository.save(club));
    }

    @Override
    public void update(String clubId, ClubUpdateRequest request) {
        //check existed club
        Club club = clubRepository.findClubByClubNameAndIsDeletedFalse(clubId.toUpperCase()).orElseThrow(() -> new AppException(ErrorCode.CLUB_NOT_FOUND));

        //update
        if (Objects.nonNull(request.getClubName())) {
            club.setClubName(request.getClubName().toUpperCase());
        }

        if (Objects.nonNull(request.getColorHex())) {
            club.setColorHex(request.getColorHex().toUpperCase());
        }

        if (Objects.nonNull(request.getShortName())) {
            club.setShortName(request.getShortName().toUpperCase());
        }

        clubRepository.save(club);
    }
}
