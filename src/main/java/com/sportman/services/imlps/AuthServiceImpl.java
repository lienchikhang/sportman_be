package com.sportman.services.imlps;

import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthResponse;
import com.sportman.entities.Role;
import com.sportman.entities.User;
import com.sportman.entities.UserRole;
import com.sportman.entities.UserRoleId;
import com.sportman.enums.EnumRole;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.AuthMapper;
import com.sportman.mappers.UserMapper;
import com.sportman.repositories.RoleRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.repositories.UserRoleRepository;
import com.sportman.services.interfaces.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    AuthMapper AuthMapper;
    PasswordEncoder passwordEncoder;
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;

    @Transactional
    @Override
    public AuthResponse register(AuthRequest request) {

        //check username & email
        if(userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) throw new AppException(ErrorCode.USER_EXISTED);

        //create new user
        User newUser = userMapper.toUser(request);
        newUser.setIsDeleted(false);

        //encode password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        //save user
        User user = userRepository.save(newUser);

        //set roles
        Role existedRole = roleRepository.findById(EnumRole.USER).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        userRoleRepository.save(
                UserRole
                .builder()
                .user(user)
                .roleName(existedRole)
                .id(UserRoleId
                        .builder()
                        .userId(user.getId())
                        .roleName(EnumRole.USER.name())
                        .build())
                .build()
        );

        return AuthMapper.toAuthRespones(user);
    }

    @Override
    public Object logIn(Object request) {
        return null;
    }

    @Override
    public Object introspectToken(Object request) {
        return null;
    }

    @Override
    public Object logOut() {
        return null;
    }
}
