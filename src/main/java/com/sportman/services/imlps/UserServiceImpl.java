package com.sportman.services.imlps;

import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthResponse;
import com.sportman.entities.Cart;
import com.sportman.entities.Role;
import com.sportman.entities.User;
import com.sportman.entities.UserRole;
import com.sportman.enums.EnumRole;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.AuthMapper;
import com.sportman.mappers.UserMapper;
import com.sportman.repositories.CartRepository;
import com.sportman.repositories.RoleRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.services.interfaces.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //repos
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    CartRepository cartRepository;

    //mappers
    UserMapper userMapper;
    AuthMapper authMapper;

    @Override
    public AuthResponse create(AuthRequest request) {
        //check username & email
        if(userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) throw new AppException(ErrorCode.USER_EXISTED);

        //create new user
        User newUser = userMapper.toUser(request);
        newUser.setIsDeleted(false);
        newUser.setBalance(0);

        //encode password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        //get role user
        Role role = roleRepository.findById(EnumRole.USER).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        HashSet<UserRole> roles = new HashSet<>();
        roles.add(UserRole
                .builder()
                .role(role)
                .user(newUser)
                .build()
        );

        //set roles
        newUser.setUserRole(roles);

        //create default cart
        User savedUser = userRepository.save(newUser);
        cartRepository.save(Cart.builder()
                .id(savedUser.getUsername())
                .user(savedUser)
                .build());

        //save user
        return authMapper.toAuthRespones(savedUser);
    }
}
