package com.salespilot.user.dto;

import com.salespilot.user.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,

        String fullName,
        String email,
        UserStatus status,
        Instant createdAt

) {
}
