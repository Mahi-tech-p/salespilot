package com.salespilot.membership.service;


import com.salespilot.membership.dto.OrganizationMemberResponse;
import com.salespilot.membership.entity.OrganizationMembership;
import com.salespilot.membership.repository.OrganizationMembershipRepository;
import com.salespilot.organization.service.OrganizationAccessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationMemberServiceImpl implements OrganizationMemberService{

    private final OrganizationMembershipRepository membershipRepository;
    private  final OrganizationAccessService organizationAccessService;


    public OrganizationMemberServiceImpl(OrganizationMembershipRepository membershipRepository, OrganizationAccessService organizationAccessService){
        this.membershipRepository =membershipRepository;
        this.organizationAccessService = organizationAccessService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationMemberResponse> getMembers(
            UUID organizationId,
            UUID authenticatedUserId,
            Pageable pageable
    ) {
        organizationAccessService.requireMembership(
                organizationId,
                authenticatedUserId
        );

        return membershipRepository.findMemberResponses(
                organizationId,
                pageable
        );
    }
}