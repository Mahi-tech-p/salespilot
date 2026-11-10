package com.salespilot.organization.service;

import com.salespilot.membership.entity.*;
import com.salespilot.membership.repository.OrganizationMembershipRepository;
import com.salespilot.organization.entity.Organization;
import com.salespilot.organization.repository.OrganizationRepository;
import com.salespilot.user.entity.User;
import com.salespilot.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrganizationPersistenceService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public OrganizationPersistenceService(
            OrganizationRepository organizationRepository,
            OrganizationMembershipRepository membershipRepository,
            UserRepository userRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Organization create(
            String name,
            String slug,
            UUID creatorId
    ) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        Organization organization = new Organization();
        organization.setName(name);
        organization.setSlug(slug);

        organizationRepository.saveAndFlush(organization);

        OrganizationMembership membership =
                new OrganizationMembership();

        membership.setUser(creator);
        membership.setOrganization(organization);
        membership.setRole(MembershipRole.OWNER);
        membership.setStatus(MembershipStatus.ACTIVE);

        membershipRepository.saveAndFlush(membership);

        return organization;
    }
}