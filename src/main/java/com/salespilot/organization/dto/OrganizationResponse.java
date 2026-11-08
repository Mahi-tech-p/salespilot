package com.salespilot.organization.dto;

import com.salespilot.organization.entity.OrganizationStatus;

import java.time.Instant;
import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name,
        String slug,
        OrganizationStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
