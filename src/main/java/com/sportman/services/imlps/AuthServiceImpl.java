package com.sportman.services.imlps;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sportman.dto.request.AuthLoginRequest;
import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthLoginResponse;
import com.sportman.dto.response.AuthResponse;
import com.sportman.dto.response.UserLoginResponse;
import com.sportman.entities.Role;
import com.sportman.entities.User;
import com.sportman.entities.UserRole;
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
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
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

    @NonFinal
    @Value("${jwt.secretkey}")
    String secretKey;


    @Transactional
    @Override
    public AuthResponse register(AuthRequest request) {

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

        //save user
        return AuthMapper.toAuthRespones(userRepository.save(newUser));
    }

    @Override
    public AuthLoginResponse logIn(AuthLoginRequest request) {

        //check user exist
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //check password valid
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new AppException(ErrorCode.USER_INVALID);

        //create claimsSets used to create token
        JWTClaimsSet claimsSetRefreshToken = createClaimsSetRefresh(user);
        JWTClaimsSet claimsSetAccessToken = createClaimsSetAccess(user, claimsSetRefreshToken.getJWTID());

        //create access & refresh token
        String accessToken = createToken(claimsSetAccessToken);
        String refreshToken = createToken(claimsSetRefreshToken);

        return AuthLoginResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userInfo(UserLoginResponse
                        .builder()
                        .avatar(user.getAvatar())
                        .balance(user.getBalance().intValue())
                        .username(user.getUsername())
                        .build())
                .build();
    }

    @Override
    public Object introspectToken(Object request) {
        return null;
    }

    @Override
    public Object logOut() {
        return null;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        user.getUserRole().forEach(userRole -> {
            stringJoiner.add("ROLE_" + userRole.getRole().getName().name());
            userRole.getRole().getRolePermissions().forEach(rolePermission -> {
                stringJoiner.add(rolePermission.getId().getPerName());
            });
        });

        return stringJoiner.toString();
    }

    private JWSHeader createHeader() {
        return new JWSHeader(JWSAlgorithm.HS512);
    }

    private JWTClaimsSet createClaimsSetAccess(User user, String refreshId) {
        return new JWTClaimsSet
                .Builder()
                .issuer("sportman")
                .issueTime(new Date())
                .subject(user.getUsername())
                .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()))
                .claim("refreshId", refreshId)
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .build();
    }

    private JWTClaimsSet createClaimsSetRefresh(User user) {
        return new JWTClaimsSet
                .Builder()
                .issuer("sportman")
                .issueTime(new Date())
                .subject(user.getUsername())
                .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId())
                .build();
    }


    private String createToken(JWTClaimsSet jwtClaimsSet) {

        //create payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        //compile header + payload
        JWSObject jwsObject = new JWSObject(createHeader(), payload);

        //sign
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }

    }

}
