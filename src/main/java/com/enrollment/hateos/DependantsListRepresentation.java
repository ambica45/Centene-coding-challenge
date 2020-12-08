package com.enrollment.hateos;

import com.enrollment.model.Dependants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

/**
 * Hateos Representation for a List of Dependants
 */
public class DependantsListRepresentation extends RepresentationModel<DependantsListRepresentation> {

    private List<Dependants> dependantsList;

    @JsonCreator
    public DependantsListRepresentation(@JsonProperty("dependants") List<Dependants> dependants) {
        this.dependantsList = dependants;
    }

    public List<Dependants> getDependants() {
        return dependantsList;
    }
}
