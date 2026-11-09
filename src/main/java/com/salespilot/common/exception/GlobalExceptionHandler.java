package com.salespilot.common.exception;


import com.salespilot.organization.exception.OrganizationSlugConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrganizationSlugConflictException.class)
    public ProblemDetail handleSlugConflict(OrganizationSlugConflictException exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage()
                );

        problemDetail.setTitle("Organization Registration Conflict");

        return problemDetail;
    }
}
