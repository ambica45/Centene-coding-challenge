package com.enrollment.exception;

/**
 * To indicate an unforeseen exception during an enrollment
 */
public class EnrollmentException extends  RuntimeException{

    String message;

    public EnrollmentException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

