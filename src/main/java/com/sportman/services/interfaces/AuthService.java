package com.sportman.services.interfaces;

import com.nimbusds.jose.JOSEException;
import com.sportman.dto.request.*;
import com.sportman.dto.response.AuthIntrospectResponse;
import com.sportman.dto.response.AuthLoginResponse;
import com.sportman.dto.response.AuthRefreshResponse;
import com.sportman.dto.response.AuthResponse;

import java.text.ParseException;

public interface AuthService {

    public AuthLoginResponse logIn(AuthLoginRequest request);

    public AuthIntrospectResponse introspectToken(AuthIntrospectRequest request);

    public void logOut(AuthLogoutRequest request) throws JOSEException, ParseException;

    public AuthRefreshResponse refresh(AuthIntrospectRequest request) throws JOSEException, ParseException;

    public void getOTP(AuthOtpRequest request);
}
