package com.salespilot.organization.service;

import com.salespilot.membership.entity.MembershipRole;

import java.util.UUID;

public interface OrganizationAccessService {

    void requireMembership(UUID organizationId,
                            UUID userId
                            );

    void requireRole(UUID organizationId,
                      UUID userId,
                      MembershipRole... allowedRoles
                      );
}
