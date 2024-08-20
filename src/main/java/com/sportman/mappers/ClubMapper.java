package com.sportman.mappers;

import com.sportman.dto.request.ClubRequest;
import com.sportman.dto.response.ClubResponse;
import com.sportman.entities.Club;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubMapper {

    public ClubResponse toClubResponse(Club club);

    public Club toClub(ClubRequest request);

}
