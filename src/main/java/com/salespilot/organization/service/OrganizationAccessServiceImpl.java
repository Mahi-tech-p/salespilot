package com.salespilot.organization.service;

import com.salespilot.membership.entity.MembershipRole;
import com.salespilot.membership.entity.MembershipStatus;
import com.salespilot.membership.entity.OrganizationMembership;
import com.salespilot.membership.repository.OrganizationMembershipRepository;
import com.salespilot.organization.entity.Organization;
import com.salespilot.organization.entity.OrganizationStatus;
import com.salespilot.organization.repository.OrganizationRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

@Service
public class OrganizationAccessServiceImpl implements OrganizationAccessService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMembershipRepository membershipRepository;

    public OrganizationAccessServiceImpl(
            OrganizationRepository organizationRepository,
            OrganizationMembershipRepository membershipRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.membershipRepository = membershipRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void requireMembership(
            UUID organizationId,
            UUID userId
    ) {
        getActiveMembership(organizationId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public void requireRole(
            UUID organizationId,
            UUID userId,
            MembershipRole... allowedRoles
    ) {
        OrganizationMembership membership =
                getActiveMembership(organizationId, userId);

        if (allowedRoles == null
                || allowedRoles.length == 0
                || Arrays.stream(allowedRoles)
                .noneMatch(role -> role == membership.getRole())) {

            throw new AccessDeniedException(
                    "Insufficient organization permissions"
            );
        }
    }

    private OrganizationMembership getActiveMembership(
            UUID organizationId,
            UUID userId
    ) {
        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "Organization access denied"
                        )
                );

        if (organization.getStatus() != OrganizationStatus.ACTIVE) {
            throw new AccessDeniedException(
                    "Organization is not active"
            );
        }

        OrganizationMembership membership = membershipRepository
                .findByUser_IdAndOrganization_Id(
                        userId,
                        organizationId
                )
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "Organization access denied"
                        )
                );

        if (membership.getStatus() != MembershipStatus.ACTIVE) {
            throw new AccessDeniedException(
                    "Membership is not active"
            );
        }

        return membership;
    }
}