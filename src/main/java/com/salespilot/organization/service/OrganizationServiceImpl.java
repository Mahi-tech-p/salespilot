package com.salespilot.organization.service;

import com.salespilot.organization.dto.CreateOrganizationRequest;
import com.salespilot.organization.dto.OrganizationResponse;
import com.salespilot.organization.entity.Organization;
import com.salespilot.organization.exception.OrganizationSlugConflictException;
import com.salespilot.organization.util.OrganizationSlugGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final int MAX_ATTEMPTS = 3;
    private static final int MAX_SLUG_LENGTH = 100;

    private final OrganizationPersistenceService persistenceService;

    public OrganizationServiceImpl(
            OrganizationPersistenceService persistenceService
    ) {
        this.persistenceService = persistenceService;
    }

    @Override
    public OrganizationResponse createOrganization(
            CreateOrganizationRequest request,
            UUID authenticatedUserId
    ) {

        String name = request.name()
                .trim()
                .replaceAll("\\s+", " ");

        String baseSlug = OrganizationSlugGenerator.generate(name);

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            String slug = attempt == 1
                    ? baseSlug
                    : generateUniqueCandidate(baseSlug);

            try {
                Organization saved =
                        persistenceService.create(name, slug , authenticatedUserId);

                return toResponse(saved);

            } catch (DataIntegrityViolationException ex) {

                if (!isSlugConflict(ex)) {
                    throw ex;
                }
            }
        }

        throw new OrganizationSlugConflictException(
                "Unable to generate a unique organization slug"
        );
    }

    private String generateUniqueCandidate(String baseSlug) {

        String suffix = "-" + UUID.randomUUID()
                .toString()
                .substring(0, 8);

        int maxBaseLength = MAX_SLUG_LENGTH - suffix.length();

        String prefix = baseSlug.substring(
                0,
                Math.min(baseSlug.length(), maxBaseLength)
        ).replaceAll("-+$", "");

        return prefix + suffix;
    }

    private boolean isSlugConflict(
            DataIntegrityViolationException ex
    ) {
        Throwable cause = ex;

        while (cause != null) {

            if (cause instanceof org.hibernate.exception.ConstraintViolationException violation) {

                return "uk_organization_slug".equalsIgnoreCase(
                        violation.getConstraintName()
                );
            }

            cause = cause.getCause();
        }

        return false;
    }

    private OrganizationResponse toResponse(Organization saved) {

        return new OrganizationResponse(
                saved.getId(),
                saved.getName(),
                saved.getSlug(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}