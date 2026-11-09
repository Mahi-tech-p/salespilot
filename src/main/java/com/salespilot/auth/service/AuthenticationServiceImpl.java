package com.salespilot.auth.service;

import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.salespilot.auth.dto.LoginRequest;
import com.salespilot.auth.dto.LoginResponse;
import com.salespilot.user.entity.User;
import com.salespilot.user.entity.UserStatus;
import com.salespilot.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtEncoder jwtEncoder,
                                     @Value("${app.jwt.expiration-seconds}") long expirationSeconds){
        this.expirationSeconds = expirationSeconds;
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder  = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest){
        String email = loginRequest.email().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BadCredentialsException("Invalid Credentials"));

        if(!passwordEncoder.matches(loginRequest.password() , user.getPasswordHash())){
            throw new BadCredentialsException("Invalid Credentials");
        }

        if(user.getStatus() != UserStatus.ACTIVE){
            throw new DisabledException("Account is Disabled");
        }

        Instant now =  Instant.now();
        Instant expiresAt = now.plus(
                expirationSeconds,
                ChronoUnit.SECONDS
        );

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("salespilot")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();

        return new LoginResponse(
                token,
                "Bearer",
                expirationSeconds
        );
    }

}
