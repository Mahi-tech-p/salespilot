package com.salespilot.auth.controller;


import com.salespilot.auth.dto.LoginRequest;
import com.salespilot.auth.dto.LoginResponse;
import com.salespilot.auth.service.AuthenticationService;
import com.salespilot.user.dto.RegisterUserRequest;
import com.salespilot.user.dto.UserResponse;
import com.salespilot.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private  final UserService userService;
    private  final AuthenticationService authenticationService;

    public AuthController(UserService userService, AuthenticationService authenticationService){
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest registerUserRequest){

        UserResponse response = userService.register(registerUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
