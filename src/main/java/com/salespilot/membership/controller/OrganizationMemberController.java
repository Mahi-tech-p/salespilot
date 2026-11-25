package com.salespilot.membership.controller;

import com.salespilot.membership.dto.OrganizationMemberResponse;
import com.salespilot.membership.service.OrganizationMemberService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/members")
public class OrganizationMemberController {

    private final OrganizationMemberService memberService;

    public OrganizationMemberController(
            OrganizationMemberService memberService
    ) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<Page<OrganizationMemberResponse>> getMembers(
            @PathVariable UUID organizationId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        Map<String, String> allowedSortFields = Map.of(
                "fullName", "user.fullName",
                "email", "user.email",
                "createdAt", "createdAt"
        );

        String sortProperty = allowedSortFields.get(sortBy);

        if (sortProperty == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported sort field"
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort direction"
            );
        }

        Sort sort = Sort.by(sortDirection, sortProperty)
                .and(Sort.by(sortDirection, "id"));

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                memberService.getMembers(
                        organizationId,
                        userId,
                        pageable
                )
        );
    }
}