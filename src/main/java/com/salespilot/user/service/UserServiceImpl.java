package com.salespilot.user.service;


import com.salespilot.user.dto.RegisterUserRequest;
import com.salespilot.user.dto.UserResponse;
import com.salespilot.user.entity.User;
import com.salespilot.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterUserRequest registerUserRequest){

        String email = registerUserRequest.email().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();

        user.setFullName(registerUserRequest.fullName());
        user.setEmail(registerUserRequest.email());
        user.setPasswordHash(passwordEncoder.encode(registerUserRequest.password()));

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getStatus(),
                savedUser.getCreatedAt()
        );
    }
}
