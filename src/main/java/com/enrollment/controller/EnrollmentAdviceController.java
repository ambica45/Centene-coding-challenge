package com.enrollment.controller;

import com.enrollment.exception.EnrollmentException;
import com.enrollment.exception.EnrollmentNotFoundException;
import com.enrollment.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EnrollmentAdviceController {
    private Logger logger = LoggerFactory.getLogger(EnrollmentAdviceController.class);


    @ResponseBody
    @ExceptionHandler(EnrollmentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String enrollmentExceptionHandler(EnrollmentException ex){
        logger.error("Handling Enrollment Exception {}" , ex.getMessage());
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String enrollmentValidationExceptionHandler(ValidationException ex){
        logger.error("Handling Validation Exception {}" , ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EnrollmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String enrollmentNotFoundExceptionHandler(EnrollmentNotFoundException ex){
        logger.error("No Enrollment Found Exception {}" , ex.getMessage());
        return ex.getMessage();
    }

}
