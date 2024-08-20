package com.sportman.mappers;

import com.sportman.dto.request.SeasonCreateRequest;
import com.sportman.dto.response.SeasonResponse;
import com.sportman.entities.Season;
import com.sportman.entities.SeasonId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SeasonMapper {

    @Mappings({
            @Mapping(target = "yearStart", source = "id.yearStart"),
            @Mapping(target = "yearEnd", source = "id.yearEnd")
    })
    public SeasonResponse toSeasonResponse(Season season);

    @Mappings({
            @Mapping(target = "id.yearStart", source = "yearStart"),
            @Mapping(target = "id.yearEnd", source = "yearEnd")
    })
    public Season toSeason(SeasonId seasonId);

    public SeasonId toSeasonId(SeasonCreateRequest request);

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    public Season toSeason(SeasonCreateRequest request);

}
