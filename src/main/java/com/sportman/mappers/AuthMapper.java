package com.sportman.mappers;

import com.sportman.dto.response.AuthResponse;
import com.sportman.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    public AuthResponse toAuthRespones(User user);

}
