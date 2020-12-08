package com.enrollment.controller;

import com.enrollment.hateos.DependantsListRepresentation;
import com.enrollment.hateos.DependatsRepresentation;
import com.enrollment.hateos.EnrolleeRepresentation;
import com.enrollment.model.Dependants;
import com.enrollment.model.Enrollee;
import com.enrollment.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

/**
 * Controller for all API Requests. Follows HATEOS standard response
 */
@RestController
public class EnrollmentController {

    private Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    EnrollmentService service;

  /*  @PostMapping("/enrollee")
    Enrollee createEnrollee(@RequestBody Enrollee enrollee) {
        logger.info("Inside Enrollment Creation");
        return service.handleCreateEnrollment(enrollee);
    }*/
  @PostMapping("/enrollee")
  HttpEntity<EnrolleeRepresentation> createEnrollee(@RequestBody Enrollee enrollee) {
      logger.info("Inside Enrollment Creation");
      Enrollee enrolleeRet =  service.handleCreateEnrollment(enrollee);
      EnrolleeRepresentation representation = new EnrolleeRepresentation(enrolleeRet);
      representation.add(linkTo(methodOn(EnrollmentController.class).getEnrollee(enrolleeRet.getId())).withRel("Get Enrollee"));
      representation.add(linkTo(methodOn(EnrollmentController.class).deleteEnrollee(enrolleeRet.getId())).withRel("Delete Enrollee"));
      representation.add(linkTo(methodOn(EnrollmentController.class).updateEnrollee(enrolleeRet.getId(),enrolleeRet)).withRel("Update Enrollee"));
      representation.add(linkTo(methodOn(EnrollmentController.class).deleteAllDependants(enrolleeRet.getId())).withRel("Delete All Dependants"));
      representation.add(linkTo(methodOn(EnrollmentController.class).createDependants(enrolleeRet.getId(),null)).withRel("Create Dependants"));
      return new ResponseEntity<>(representation, HttpStatus.OK);
  }

    @GetMapping("/enrollee/{id}")
    HttpEntity<EnrolleeRepresentation> getEnrollee(@PathVariable Long id) {
        logger.info("Inside Get Enrollment ");
        Enrollee enrollee= service.getEnrollee(id);
        EnrolleeRepresentation representation = new EnrolleeRepresentation(enrollee);
        representation.add(linkTo(methodOn(EnrollmentController.class).deleteEnrollee(enrollee.getId())).withRel("Delete Enrollee"));
        representation.add(linkTo(methodOn(EnrollmentController.class).updateEnrollee(enrollee.getId(),enrollee)).withRel("Update Enrollee"));
        representation.add(linkTo(methodOn(EnrollmentController.class).createDependants(enrollee.getId(),null)).withRel("Create Dependants"));
        representation.add(linkTo(methodOn(EnrollmentController.class).deleteAllDependants(enrollee.getId())).withRel("Delete All Dependants"));
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    @DeleteMapping("/enrollee/{id}")
    HttpEntity<EnrolleeRepresentation> deleteEnrollee(@PathVariable Long id) {
        logger.info("Inside Enrollment Deletion");
        Enrollee enrollee=  service.deleteEnrollee(id);
        EnrolleeRepresentation representation = new EnrolleeRepresentation(enrollee);
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    @PostMapping("/enrollee/{id}")
    HttpEntity<EnrolleeRepresentation> updateEnrollee(@PathVariable Long id, @RequestBody Enrollee enrollee) {
        logger.info("Inside Enrollment Deletion");
        Enrollee enrolleeRet = service.handleUpdateEnrollee(id, enrollee);
        EnrolleeRepresentation representation = new EnrolleeRepresentation(enrolleeRet);
        representation.add(linkTo(methodOn(EnrollmentController.class).getEnrollee(enrolleeRet.getId())).withRel("Get Enrollee"));
        representation.add(linkTo(methodOn(EnrollmentController.class).deleteEnrollee(enrolleeRet.getId())).withRel("Delete Enrollee"));
        representation.add(linkTo(methodOn(EnrollmentController.class).updateEnrollee(enrolleeRet.getId(),enrolleeRet)).withRel("Update Enrollee"));
        representation.add(linkTo(methodOn(EnrollmentController.class).createDependants(enrolleeRet.getId(),null)).withRel("Create Dependants"));
        representation.add(linkTo(methodOn(EnrollmentController.class).deleteAllDependants(enrolleeRet.getId())).withRel("Delete All Dependants"));
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    @PostMapping("/enrollee/{id}/dependants")
    HttpEntity<DependantsListRepresentation> createDependants(@PathVariable Long id, @RequestBody List<Dependants> dependants){
    logger.info("Inside Dependant Creation");
    List<Dependants> dependantsList =  service.handleCreateDependants(id, dependants);
        DependantsListRepresentation representation = new DependantsListRepresentation(dependantsList);
        for(Dependants tempDep : dependantsList){
            representation.add(linkTo(methodOn(EnrollmentController.class).deleteDependant(id,tempDep.getId())).withRel("Delete the Dependant"));
            representation.add(linkTo(methodOn(EnrollmentController.class).updateDependants(id,tempDep.getId(),tempDep)).withRel("Update the Dependant"));
        }
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    @DeleteMapping("/enrollee/{id}/dependants")
    HttpEntity<DependantsListRepresentation>  deleteAllDependants(@PathVariable Long id) {
        logger.info("Inside All Dependants Deletion");
        List<Dependants> dependantsList =  service.handleDeleteAllDependantsForAnEnrolle(id);
        DependantsListRepresentation representation = new DependantsListRepresentation(dependantsList);
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    @DeleteMapping("/enrollee/{id}/dependants/{dependentId}")
        HttpEntity<DependatsRepresentation> deleteDependant(@PathVariable Long id, @PathVariable Long dependentId) {
        logger.info("Inside A Dependants Deletion");
        Dependants dependants= service.handleDeleteDependant(id,dependentId);
        DependatsRepresentation representation = new DependatsRepresentation(dependants);
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    @PostMapping("/enrollee/{id}/dependants/{dependentId}")
    HttpEntity<DependatsRepresentation> updateDependants(@PathVariable Long id, @PathVariable Long dependentId, @RequestBody Dependants dependants) {
        logger.info("Inside Enrollment Deletion");
        Dependants dependantsRet= service.updateDependant(id, dependentId,dependants);
        DependatsRepresentation representation = new DependatsRepresentation(dependants);
        representation.add(linkTo(methodOn(EnrollmentController.class).deleteDependant(id,dependantsRet.getId())).withRel("Delete the Dependant"));
        return new ResponseEntity<>(representation, HttpStatus.OK);
    }
}
