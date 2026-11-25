package com.salespilot.membership.dto;

import com.salespilot.membership.entity.MembershipRole;
import com.salespilot.membership.entity.MembershipStatus;

import java.util.UUID;

public record OrganizationMemberResponse(
        UUID memberShipId,
        UUID userId,
        String fullName,
        String email,
        MembershipRole role,
        MembershipStatus status

) {
}
