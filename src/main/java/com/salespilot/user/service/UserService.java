package com.salespilot.user.service;

import com.salespilot.user.dto.RegisterUserRequest;
import com.salespilot.user.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterUserRequest registerUserRequest);
}
