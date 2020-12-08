package com.enrollment.exception;

/**
 * Indicates any errors during an input validation
 */
public class ValidationException extends RuntimeException{

    String message;

    public ValidationException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
