package com.salespilot.organization.exception;


public class OrganizationSlugConflictException  extends RuntimeException{

    public OrganizationSlugConflictException(String message){
        super(message);
    }
}
