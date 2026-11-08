package com.salespilot.organization.service;


import com.salespilot.organization.dto.CreateOrganizationRequest;
import com.salespilot.organization.dto.OrganizationResponse;
import com.salespilot.organization.entity.Organization;
import com.salespilot.organization.repository.OrganizationRepository;
import com.salespilot.organization.util.OrganizationSlugGenerator;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl  implements  OrganizationService{

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl( OrganizationRepository organizationRepository){
        this.organizationRepository = organizationRepository;
    }

    @Override
    public OrganizationResponse createOrganization(CreateOrganizationRequest request){

        String name = request.name().trim().replaceAll("\\s+", "");

        String slug = OrganizationSlugGenerator.generate(name);

        if(organizationRepository.existsBySlug(slug)){
            throw  new IllegalStateException(
                    "Organization slug already exists"
            );
        }

        Organization organization = new Organization();
        organization.setName(name);
        organization.setSlug(slug);

        Organization savedOrganization = organizationRepository.save(organization);

        return new OrganizationResponse(
                savedOrganization.getId(),
                savedOrganization.getName(),
                savedOrganization.getSlug(),
                savedOrganization.getStatus(),
                savedOrganization.getCreatedAt(),
                savedOrganization.getUpdatedAt()
        );
    }



}
