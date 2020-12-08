package com.enrollment.hateos;

import com.enrollment.model.Enrollee;
import org.springframework.hateoas.RepresentationModel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Hatoes Representation for an Enrollee
 */
public class EnrolleeRepresentation extends RepresentationModel<EnrolleeRepresentation> {

    private Enrollee enrollee;

    @JsonCreator
    public EnrolleeRepresentation(@JsonProperty("enrollee") Enrollee enrollee) {
        this.enrollee = enrollee;
    }

    public Enrollee getEnrollee() {
        return enrollee;
    }
}

