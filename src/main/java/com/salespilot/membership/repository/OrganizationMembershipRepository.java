package com.salespilot.membership.repository;

import com.salespilot.membership.entity.OrganizationMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationMembershipRepository extends JpaRepository<OrganizationMembership, UUID> {

    List<OrganizationMembership> findByUser_id(UUID userId);

    List<OrganizationMembership> findByOrganization_id(UUID organizationId);

    Optional<OrganizationMembership> findByUser_IdAndOrganization_Id(UUID userId, UUID organizationId);

    boolean existsByUser_IdAndOrganization_Id( UUID userId, UUID organizationId);

}
