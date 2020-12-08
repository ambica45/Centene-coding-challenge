package com.enrollment.service;

import com.enrollment.application.EnrollmentApplication;
import com.enrollment.dao.EnrolleeDao;
import com.enrollment.exception.EnrollmentNotFoundException;
import com.enrollment.model.Dependants;
import com.enrollment.model.Enrollee;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {EnrollmentApplication.class})
public class EnrollmentServiceTest {

    @Autowired
    EnrollmentService enrollmentService;

    Enrollee createEnrollee(String name, String dob, boolean activationStatus){
        Enrollee enrollee = new Enrollee();
        enrollee.setActivationStatus(activationStatus);
        enrollee.setName(name);
        enrollee.setPhoneNumber(9080);
        enrollee.setBirthDate(dob);
        return enrollee;
    }

    Dependants createDependants(String name, String dob, long enrollmentId){
        Dependants dependants = new Dependants();
        dependants.setEnrollmentId(enrollmentId);
        dependants.setName(name);
        dependants.setBirthDate(dob);
        return dependants;
    }
    @Test
    void createEnrollmentTest() {
        Enrollee enrollee = createEnrollee("TestEnrollee", "05-05-2010", false);
        Enrollee result = enrollmentService.handleCreateEnrollment(enrollee);
        Assert.assertTrue(result.getId() != 0);
    }

    @Test
    void getEnrollmentTest() {
        Enrollee enrollee = createEnrollee("TestEnrollee1", "05-05-2010", false);
        Enrollee result = enrollmentService.handleCreateEnrollment(enrollee);
        Assert.assertNotNull(enrollmentService.getEnrollee(result.getId()));
    }

    @Test
    void deleteEnrollee(){
        Enrollee enrollee = createEnrollee("TestEnrollee1", "05-05-2010", false);
        Enrollee result = enrollmentService.handleCreateEnrollment(enrollee);
        enrollmentService.deleteEnrollee(result.getId());
      try{
          enrollmentService.getEnrollee(result.getId());
          Assert.fail("Should not have come here");
      }
      catch (Exception e){
          Assert.assertTrue(e instanceof EnrollmentNotFoundException);
      }
    }

    @Test
    void updateEnrollee(){
        Enrollee enrollee = createEnrollee("TestEnrollee1", "05-05-2010", false);
        Enrollee result = enrollmentService.handleCreateEnrollment(enrollee);
        result.setName("TestEnrolleeUpdated");
        enrollmentService.handleUpdateEnrollee(result.getId(),result);
        Enrollee updated = enrollmentService.handleCreateEnrollment(result);
        Assert.assertTrue(updated.getName().equals("TestEnrolleeUpdated"));
    }

    @Test
    void createDependant(){
        Enrollee enrollee = createEnrollee("TestEnrollee2", "05-05-2010", false);
        Enrollee result = enrollmentService.handleCreateEnrollment(enrollee);
        Dependants dependants = createDependants( "Dependant1", "05-05-2020",result.getId());
        Dependants dependants2 = createDependants( "Dependant2", "05-06-2020",result.getId());
        List<Dependants> dependantsList = enrollmentService.handleCreateDependants(result.getId(), Arrays.asList(dependants,dependants2));
        Assert.assertTrue(dependantsList.get(0).getEnrollmentId()== result.getId());
    }

    @Test
    void deleteDependants(){
        Enrollee enrollee = createEnrollee("TestEnrollee2", "05-05-2010", false);
        Enrollee result = enrollmentService.handleCreateEnrollment(enrollee);
        Dependants dependants = createDependants( "Dependant1", "05-05-2020",result.getId());
        Dependants dependants2 = createDependants( "Dependant2", "05-06-2020",result.getId());
        List<Dependants> dependantsList = enrollmentService.handleCreateDependants(result.getId(), Arrays.asList(dependants,dependants2));
        enrollmentService.handleDeleteDependant(result.getId(),dependantsList.get(0).getId());
        Assert.assertTrue(enrollmentService.getEnrollee(result.getId()).getDependantsList().size()==1);
    }
}
