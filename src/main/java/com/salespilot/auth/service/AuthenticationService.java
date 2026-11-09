package com.salespilot.auth.service;

import com.salespilot.auth.dto.LoginRequest;
import com.salespilot.auth.dto.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
}
