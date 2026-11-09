package com.salespilot.organization.service;


import com.salespilot.organization.entity.Organization;
import com.salespilot.organization.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationPersistenceService {
    private final OrganizationRepository organizationRepository;

    public OrganizationPersistenceService( OrganizationRepository organizationRepository){
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Organization create(String name , String slug){

        Organization organization = new Organization();
        organization.setSlug(slug);
        organization.setName(name);

        return organizationRepository.saveAndFlush(organization);
    }
}
