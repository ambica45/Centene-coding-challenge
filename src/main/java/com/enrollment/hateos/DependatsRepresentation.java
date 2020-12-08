package com.enrollment.hateos;

import com.enrollment.model.Dependants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

/**
 * Hateos Representation for a Dependant
 */
public class DependatsRepresentation extends RepresentationModel<DependantsListRepresentation> {
    private Dependants dependants;

    @JsonCreator
    public DependatsRepresentation(@JsonProperty("dependants") Dependants dependants) {
        this.dependants = dependants;
    }
    public Dependants getDependants() {
        return dependants;
    }
}
