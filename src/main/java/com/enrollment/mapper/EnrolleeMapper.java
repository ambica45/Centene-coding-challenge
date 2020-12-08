package com.enrollment.mapper;

import com.enrollment.model.Enrollee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL Result set mapper for an enrollee
 */
public class EnrolleeMapper implements RowMapper<Enrollee> {
    private Logger logger = LoggerFactory.getLogger(EnrolleeMapper.class);

    @Override
    public Enrollee mapRow(ResultSet resultSet, int i) throws SQLException {

        Enrollee enrollee = new Enrollee();
        enrollee.setId(resultSet.getLong("id"));
        enrollee.setActivationStatus(resultSet.getBoolean("activationStatus"));
        enrollee.setBirthDate(resultSet.getString("birthDate"));
        enrollee.setPhoneNumber(resultSet.getInt("phoneNumber"));
        enrollee.setName(resultSet.getString("name"));
        logger.info("Enrollee mapped from Result Set {}", enrollee);
        return enrollee;
    }
}
