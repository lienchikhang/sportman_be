package com.sportman.services.imlps;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sportman.dto.request.*;
import com.sportman.dto.response.*;
import com.sportman.entities.BlackListToken;
import com.sportman.entities.Role;
import com.sportman.entities.User;
import com.sportman.entities.UserRole;
import com.sportman.enums.EnumRole;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.AuthMapper;
import com.sportman.mappers.UserMapper;
import com.sportman.repositories.BlackListRepository;
import com.sportman.repositories.RoleRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.repositories.UserRoleRepository;
import com.sportman.services.interfaces.AuthService;
import com.sportman.services.interfaces.MailService;
import com.sportman.services.interfaces.RedisService;
import com.sportman.utils.OtpUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    //repos
    UserRepository userRepository;
    BlackListRepository blackListRepository;

    //services
    RedisService redisService;
    MailService mailService;

    @NonFinal
    @Value("${jwt.secretkey}")
    String secretKey;


    @Override
    public AuthLoginResponse logIn(AuthLoginRequest request) {

        //check user exist
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //check password valid
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
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
    public AuthIntrospectResponse introspectToken(AuthIntrospectRequest request)  {

        //get token
        String token = request.getToken();

        try {
            SignedJWT signedJWT = verifyToken(token);

        } catch (JOSEException | ParseException e) {
            return AuthIntrospectResponse.builder().auth(false).build();
        }

        return AuthIntrospectResponse.builder().auth(true).build();
    }

    @Override
    public void logOut(AuthLogoutRequest request) throws JOSEException, ParseException {

        //get token
        String token = request.getToken();

        //get signedJWT
        SignedJWT signedJWT = verifyToken(token);

        //get necessary claims
        String refreshId = signedJWT.getJWTClaimsSet().getClaim("refreshId").toString();
        Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();

        //save refreshId into blacklist
        blackListRepository.save(
                BlackListToken
                        .builder()
                        .refreshId(refreshId)
                        .expirationTime(exp)
                        .build()
        );

    }

    @Override
    public AuthRefreshResponse refresh(AuthIntrospectRequest request) throws ParseException {

        //get token
        String refreshToken = request.getToken();

        //get signJwt
        SignedJWT signedJWT = parseIntoSignJWT(refreshToken);

        //check is refresh token?
        if (Objects.nonNull(signedJWT.getJWTClaimsSet().getClaim("refreshId"))) throw new AppException(ErrorCode.WRONG_TOKEN);

        //validate
        try {
            verifyRefreshToken(refreshToken);
        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }

        //save refresh token into blacklist
        blackListRepository.save(BlackListToken.builder().expirationTime(signedJWT.getJWTClaimsSet().getExpirationTime()).refreshId(signedJWT.getJWTClaimsSet().getJWTID()).build());

        User user = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //create jwtClaimsSets
        JWTClaimsSet jwtRefreshTokenClaimsSet = createClaimsSetRefresh(user);
        JWTClaimsSet jwtAccessTokenClaimsSet = createClaimsSetAccess(user, jwtRefreshTokenClaimsSet.getJWTID());

        //create new tokens
        String newAccessToken = createToken(jwtAccessTokenClaimsSet);
        String newRefreshToken = createToken(jwtRefreshTokenClaimsSet);

        return AuthRefreshResponse
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void getOTP(AuthOtpRequest request) {

        //check email exist
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.USER_EXISTED);

        //get OTP
        String otp = OtpUtils.create();
        if (Objects.isNull(otp)) throw new AppException(ErrorCode.OTP_INVALID);

        //save otp into redis
        redisService.saveKey(request.getEmail(), otp, 75000L);

        //send mail
        mailService.send(request.getEmail(), "OTP - Sportman", otp);

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

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());

        SignedJWT signedJWT = parseIntoSignJWT(token);

        //check:: has refreshId existed in blacklist
        if (blackListRepository.existsById(signedJWT.getJWTClaimsSet().getClaim("refreshId").toString()))
            throw new AppException(ErrorCode.UN_AUTHENTICATED);;


        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verify = signedJWT.verify(jwsVerifier);

        if (!(verify && expiredTime.after(new Date()))) throw new AppException(ErrorCode.UN_AUTHENTICATED);

        return signedJWT;

    }

    private SignedJWT verifyRefreshToken(String token) throws JOSEException, ParseException {

        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());

        SignedJWT signedJWT = parseIntoSignJWT(token);

        //check:: has refreshId existed in blacklist
        if (blackListRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UN_AUTHENTICATED);;


        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verify = signedJWT.verify(jwsVerifier);

        if (!(verify && expiredTime.after(new Date()))) throw new AppException(ErrorCode.UN_AUTHENTICATED);

        return signedJWT;

    }

    private SignedJWT parseIntoSignJWT(String token) throws ParseException {
        return SignedJWT.parse(token);
    }
}
