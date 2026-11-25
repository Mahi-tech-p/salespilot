package com.salespilot.membership.repository;

import com.salespilot.membership.dto.OrganizationMemberResponse;
import com.salespilot.membership.entity.OrganizationMembership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationMembershipRepository extends JpaRepository<OrganizationMembership, UUID> {

    List<OrganizationMembership> findByUser_id(UUID userId);


    Optional<OrganizationMembership> findByUser_IdAndOrganization_Id(UUID userId, UUID organizationId);

    boolean existsByUser_IdAndOrganization_Id( UUID userId, UUID organizationId);
    Page<OrganizationMembership> findByOrganization_Id(
            UUID organizationId,
            Pageable pageable
    );

    @Query(
            value = """
        SELECT new com.salespilot.membership.dto.OrganizationMemberResponse(
            m.id,
            u.id,
            u.fullName,
            u.email,
            m.role,
            m.status
        )
        FROM OrganizationMembership m
        JOIN m.user u
        WHERE m.organization.id = :organizationId
        """,
            countQuery = """
        SELECT COUNT(m)
        FROM OrganizationMembership m
        WHERE m.organization.id = :organizationId
        """
    )
    Page<OrganizationMemberResponse> findMemberResponses(
            @Param("organizationId") UUID organizationId,
            Pageable pageable
    );
}
