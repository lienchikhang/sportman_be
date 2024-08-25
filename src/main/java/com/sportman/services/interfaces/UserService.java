package com.sportman.services.interfaces;

import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.AuthResponse;

public interface UserService {

    public AuthResponse create(AuthRequest request);


}
