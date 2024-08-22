package com.sportman.mappers;

import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthResponse;
import com.sportman.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public User toUser(AuthRequest request);

    public AuthResponse toUserResponse(User user);

}
