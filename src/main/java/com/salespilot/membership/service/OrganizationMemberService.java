package com.salespilot.membership.service;

import com.salespilot.membership.dto.OrganizationMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrganizationMemberService {

    Page<OrganizationMemberResponse> getMembers(
            UUID organizationId,
            UUID authenticatedUserId,
            Pageable pageable
    );
}