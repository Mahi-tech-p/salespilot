package com.salespilot.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(
        @NotBlank(message = "Organization name is required")
        @Size( min = 2, max = 150,
        message = "Organization name must be 2 to 150 characters"
        )
        String name
) {

}
