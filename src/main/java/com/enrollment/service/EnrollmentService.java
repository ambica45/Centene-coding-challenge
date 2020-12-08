package com.enrollment.service;

import com.enrollment.dao.EnrolleeDao;
import com.enrollment.exception.EnrollmentException;
import com.enrollment.exception.EnrollmentNotFoundException;
import com.enrollment.exception.ValidationException;
import com.enrollment.model.Dependants;
import com.enrollment.model.Enrollee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Orchestration layer between API and the Data Access Layer
 */
@Service
public class EnrollmentService {
    private Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    EnrolleeDao enrolleeDao;

    public Enrollee handleCreateEnrollment(Enrollee enrollee){

        List<String> errors = new ArrayList<>();
        if(null == enrollee.getBirthDate()){
            errors.add("BirthDate cannot be Empty");
        }
        if(null == enrollee.getName()){
            errors.add("Name cannot be Empty");
        }
        if(null == enrollee.getActivationStatus()){
            errors.add("Activation Status cannot be Empty");
        }
        if(errors.size() > 0){
            logger.error("Validation errors during enrollement are {}" ,errors );
            throw new ValidationException(errors.toString());
        }
        Enrollee enrolleReturned =  enrolleeDao.addEnrollee(enrollee);
        if(enrolleReturned.getId()==0){
            logger.error("Error in creating enrollment");
            throw new EnrollmentException("Error in creating Enrollee");
        }
        return enrolleReturned;
    }

    public Enrollee getEnrollee(long id){
        validateEnrollmentId(id);
        Enrollee retEnrollee;
        try {
            retEnrollee = enrolleeDao.getEnrollee(id);
        }
        catch (Exception ex){
            logger.error("Enrollee not found for the id {}" , id);
            throw new EnrollmentNotFoundException("Enrollee not found for the id " + id);
        }
        return retEnrollee;
    }

    public Enrollee deleteEnrollee(long id){
       Enrollee enrollee = enrolleeDao.removeEnrollee(id);

       if(enrollee==null){
           logger.error("Enrollee cannot be deleted for the enrollee id {}", id);
           throw new EnrollmentException(("Error in Deleting Enrollee"));
       }
        return enrollee;
    }

    public Enrollee handleUpdateEnrollee(long id, Enrollee enrollee) {
        validateEnrollmentId(id);
        enrollee.setId(id);
        Enrollee returnEnrollee = enrolleeDao.updateEnrollee(enrollee);
        if(returnEnrollee==null){
            logger.error("Enrollee not found for the id {}", id);
            throw new EnrollmentNotFoundException("Enrollee not found for the id " + enrollee.getId());
        }
        return enrollee;
    }

    public List<Dependants> handleCreateDependants(Long id, List<Dependants> dependants) {
        validateEnrollmentId(id);
        List<Dependants> dependantsList;
        try{
            dependantsList = enrolleeDao.addDependants(id,dependants);
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            throw new EnrollmentNotFoundException(ex.getMessage());
        }
        return dependantsList;
    }
    public List<Dependants> handleDeleteAllDependantsForAnEnrolle(Long id) {
        validateEnrollmentId(id);
        List<Dependants> dependantsList;
        try{
            dependantsList = enrolleeDao.removeAllDependentsForAnEnrollee(id);
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            throw new EnrollmentNotFoundException("Enrollee not found for the id " + id);
        }
        return dependantsList;
    }
    public Dependants handleDeleteDependant(Long id, Long dependentId) {
        validateEnrollmentId(id);
        Dependants dependants;
        try{
            dependants = enrolleeDao.removeDependants(id,dependentId);
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            throw new EnrollmentNotFoundException(ex.getMessage());
        }
        return dependants;
    }

    public Dependants updateDependant(long id, long dependentId, Dependants dependants){
        validateEnrollmentId(id);
        dependants.setId(dependentId);
        Dependants dependantRes;
        try{
            dependantRes = enrolleeDao.updateDependants(id,dependants);
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            throw new EnrollmentNotFoundException(ex.getMessage());
        }
        return dependantRes;
    }


    private void validateEnrollmentId(Long id) {
        if (id == 0) {
            logger.error("EnrolleeID cannot be 0");
            throw new ValidationException("EnrolleeID cannot be 0");
        }
    }
}
