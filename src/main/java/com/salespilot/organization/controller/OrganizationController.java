package com.salespilot.organization.controller;


import com.salespilot.organization.dto.CreateOrganizationRequest;
import com.salespilot.organization.dto.OrganizationResponse;
import com.salespilot.organization.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService){
        this.organizationService = organizationService;
    }


    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(@Valid @RequestBody CreateOrganizationRequest createOrganizationRequest){

        OrganizationResponse response = organizationService.createOrganization(createOrganizationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
