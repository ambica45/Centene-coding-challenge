package com.enrollment.mapper;

import com.enrollment.controller.EnrollmentAdviceController;
import com.enrollment.model.Dependants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL Result mapper for a Dependant
 */
public class DependantMapper implements RowMapper<Dependants> {
    private Logger logger = LoggerFactory.getLogger(DependantMapper.class);

    @Override
    public Dependants mapRow(ResultSet resultSet, int i) throws SQLException {

        Dependants dependants = new Dependants();
        dependants.setId(resultSet.getLong("id"));
        dependants.setEnrollmentId(resultSet.getLong("enrollmentId"));
        dependants.setName(resultSet.getString("name"));
        dependants.setBirthDate(resultSet.getString("birthDate"));
        logger.info("Dependants mapped from Result Set {}", dependants);
        return dependants;

    }
}
