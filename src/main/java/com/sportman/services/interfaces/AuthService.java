package com.sportman.services.interfaces;

import com.sportman.dto.request.AuthLoginRequest;
import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthLoginResponse;
import com.sportman.dto.response.AuthResponse;

public interface AuthService {

    public AuthResponse register(AuthRequest request);

    public AuthLoginResponse logIn(AuthLoginRequest request);

    public Object introspectToken(Object request);

    public Object logOut();

    private String createToken(Object user) {
        return null;
    }
}
