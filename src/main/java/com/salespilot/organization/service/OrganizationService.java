package com.salespilot.organization.service;

import com.salespilot.organization.dto.CreateOrganizationRequest;
import com.salespilot.organization.dto.OrganizationResponse;

public interface OrganizationService {

    OrganizationResponse createOrganization(CreateOrganizationRequest request);
}
