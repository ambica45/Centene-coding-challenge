package com.enrollment.exception;

/**
 * Indicates an error that an expected object is not found
 */
public class EnrollmentNotFoundException extends RuntimeException {

    String message;

    public EnrollmentNotFoundException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

