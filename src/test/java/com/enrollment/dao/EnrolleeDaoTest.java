package com.enrollment.dao;
import com.enrollment.application.EnrollmentApplication;
import com.enrollment.model.Dependants;
import com.enrollment.model.Enrollee;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import java.util.Arrays;
import java.util.List;

@JdbcTest
@Sql({"classpath:scripts/create_table.sql"})
@ContextConfiguration(classes = EnrollmentApplication.class)
public class EnrolleeDaoTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    EnrolleeDaoImpl enrolleeDao;

    @Test
    void testEnrolleeCreation() {
        Enrollee enrollee = createEnrollee();
        Assertions.assertNotNull(enrolleeDao.getEnrollee(enrollee.getId()));
    }

    @Test
    void testDeleteEnrollee() {
        Enrollee enrollee = createEnrollee();
        enrolleeDao.removeEnrollee(enrollee.getId());
        try {
            enrolleeDao.getEnrollee(enrollee.getId());
        } catch (Exception ex) {
            Assertions.assertTrue(ex instanceof IllegalArgumentException);
        }

    }

    @Test
    void testDependantCreation() {
        Enrollee enrollee = createEnrollee();
        Dependants dependants = createDependant(enrollee.getId(),"Test1" , "05-05-2019");
        List<Dependants> dependantsList = enrolleeDao.addDependants(enrollee.getId(), Arrays.asList(dependants));
        for (Dependants dependantsTemp : dependantsList) {
            Assertions.assertNotNull(dependantsTemp.getId());
        }
    }
    @Test
    void testDeleteEnrolleeAndItsDependants(){

        Enrollee enrollee = createEnrollee();
        Dependants dependants = createDependant(enrollee.getId(),"TestDel1", "05-05-2020");
        List<Dependants> dependantsList = enrolleeDao.addDependants(enrollee.getId(), Arrays.asList(dependants));
        enrolleeDao.removeEnrollee(enrollee.getId());
        List<Dependants> dependantsList1 = enrolleeDao.getDependantsForAnEnrollee(enrollee.getId());
        Assertions.assertTrue(dependantsList1==null || dependantsList1.size()==0);

    }
    @Test
    void testAddMultipleDependants(){

        Enrollee enrollee = createEnrollee();
        Dependants dependants = createDependant(enrollee.getId(),"TestDel1", "05-05-2020");
        Dependants dependants1 = createDependant(enrollee.getId(),"TestDel2", "05-05-2010");
        List<Dependants> dependantsList = enrolleeDao.addDependants(enrollee.getId(), Arrays.asList(dependants,dependants1));
        Assertions.assertTrue( dependantsList.size()==2);
    }

    @Test
    void testAddMultipleDependantsAndDeleteOne(){

        Enrollee enrollee = createEnrollee();
        Dependants dependants = createDependant(enrollee.getId(),"TestDel1", "05-05-2020");
        Dependants dependants1 = createDependant(enrollee.getId(),"TestDel2", "05-05-2010");
        List<Dependants> dependantsList = enrolleeDao.addDependants(enrollee.getId(), Arrays.asList(dependants,dependants1));
        enrolleeDao.removeDependants(enrollee.getId(), dependantsList.get(0).getId());
        Assertions.assertTrue( enrolleeDao.getDependantsForAnEnrollee(enrollee.getId()).size()==1);
    }

    @Test
    void testUpdateDependants(){

        Enrollee enrollee = createEnrollee();
        Dependants dependants = createDependant(enrollee.getId(),"TestDel1", "05-05-2020");
        Dependants dependants1 = createDependant(enrollee.getId(),"TestDel2", "05-05-2010");
        List<Dependants> dependantsList = enrolleeDao.addDependants(enrollee.getId(), Arrays.asList(dependants,dependants1));
        dependantsList.get(0).setName("New Name");
        enrolleeDao.updateDependants(enrollee.getId(), dependantsList.get(0));
        Dependants updated = enrolleeDao.getDependant(enrollee.getId(),dependantsList.get(0).getId());
        Assertions.assertTrue( updated.getName().equals("New Name"));
    }

    private Enrollee createEnrollee() {
        Enrollee enrollee = new Enrollee();
        enrollee.setActivationStatus(false);
        enrollee.setBirthDate("01-01-1988");
        enrollee.setName("TestEnrollee");
        enrollee.setPhoneNumber(408592198);
        return enrolleeDao.addEnrollee(enrollee);
    }

    private Dependants createDependant(long enrolleeId, String name, String birthdate){
        Dependants dependants = new Dependants();
        dependants.setBirthDate(birthdate);
        dependants.setName(name);
        dependants.setEnrollmentId(enrolleeId);
        return dependants;
    }
}
