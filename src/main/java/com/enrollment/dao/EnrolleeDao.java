package com.enrollment.dao;

import com.enrollment.model.Dependants;
import com.enrollment.model.Enrollee;

import java.util.List;

public interface EnrolleeDao {

    Enrollee removeEnrollee(long enrolleeId);

    Enrollee updateEnrollee(Enrollee enrollee);

    Enrollee addEnrollee(Enrollee enrollee);

    List<Dependants> addDependants(long enrolleeId, List<Dependants> dependants);

    Dependants removeDependants(long enrollmentId, long dependantId);

    List<Dependants> removeAllDependentsForAnEnrollee(long enrolleeId) ;

    Dependants updateDependants(long enrollmentId,Dependants dependants);

    Enrollee getEnrollee(long enrolleeId);

    List<Dependants> getDependantsForAnEnrollee(long enrolleeId);

    Dependants getDependant(long enrollmentId,long dependantId);

}
