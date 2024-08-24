package com.sportman.mappers;

import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthResponse;
import com.sportman.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "userRole", ignore = true)
    public User toUser(AuthRequest request);

    public AuthResponse toUserResponse(User user);

}
