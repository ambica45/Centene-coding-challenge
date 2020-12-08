package com.enrollment.dao;

import com.enrollment.controller.EnrollmentController;
import com.enrollment.mapper.DependantMapper;
import com.enrollment.mapper.EnrolleeMapper;
import com.enrollment.model.Dependants;
import com.enrollment.model.Enrollee;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class EnrolleeDaoImpl implements EnrolleeDao {
    private Logger logger = LoggerFactory.getLogger(EnrolleeDaoImpl.class);

    private final String SQL_DELETE_ENROLLEE = "delete from enrollee where id = ?";
    private final String SQL_DELETE_DEPENDANTS = "delete from dependants where id = ? and enrollmentId=?";
    private final String SQL_DELETE_DEPENDANTS_FOR_ENROLLEE = "delete from dependants where enrollmentId=?";
    private final String SQL_UPDATE_ENROLLEE = "update enrollee set name = ?, activationStatus = ?, birthDate  = ? , phoneNumber = ? where id = ?";
    private final String SQL_UPDATE_DEPENDANTS = "update dependants set name = ?, birthDate  = ?  where id = ?";
    private final String SQL_GET_ENROLLEE = "select * from enrollee where id = ?";
    private final String SQL_GET_DEPENDANT = "select * from dependants where id = ? and enrollmentId=?";
    private final String SQL_GET_ALL_DEPENDANT_FOR_ENROLLEE = "select * from dependants where enrollmentId = ?";
    private final String SQL_CREATE_DEPENDANT = "insert into dependants(enrollmentId, name, birthDate) values(?,?,?)";
    private final String SQL_CREATE_ENROLLEE = "insert into enrollee(name, birthDate,activationStatus,phoneNumber) values(?,?,?,?)";
    JdbcTemplate jdbcTemplate;

    @Autowired
    public EnrolleeDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public EnrolleeDaoImpl() {

    }

    @Override
    @Transactional
    public Enrollee removeEnrollee(long enrolleeId) {
        logger.info("Enrollment Id received in DAO layer is {}", enrolleeId);
        Enrollee enrollee = getEnrollee(enrolleeId);

        if(enrollee==null){
            throw new IllegalArgumentException("EnrolleeID not Found");
        }

        List<Dependants> dependants = getDependantsForAnEnrollee(enrolleeId);
        logger.info("Total Dependants for Enrollment Id {} is ", enrolleeId, dependants!=null? dependants.size():0);

        if (dependants != null && dependants.size() > 0) {
            jdbcTemplate.batchUpdate(SQL_DELETE_DEPENDANTS, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, dependants.get(i).getId());
                    ps.setLong(2, dependants.get(i).getEnrollmentId());

                }

                @Override
                public int getBatchSize() {
                    return dependants.size();
                }
            });
        }
        enrollee.setDependantsList(dependants);
        jdbcTemplate.update(SQL_DELETE_ENROLLEE, enrolleeId) ;
        logger.info("Enrollment deleted is {}", enrollee);
        return enrollee;
    }

    @Override
    public Enrollee updateEnrollee(Enrollee enrollee) {
        logger.info("Enrollment Id received in DAO layer is {}", enrollee.getId());
        Enrollee enrolleeFromDB = getEnrollee(enrollee.getId());
        if(enrolleeFromDB==null){
            throw new IllegalArgumentException("EnrolleeID not Found");
        }
        if (StringUtils.isEmpty(enrollee.getBirthDate()) ) {
            enrollee.setBirthDate(enrolleeFromDB.getBirthDate());
        }
        if (enrollee.getActivationStatus() == null) {
            enrollee.setActivationStatus(enrolleeFromDB.getActivationStatus());
        }
        if (enrollee.getPhoneNumber() == 0) {
            enrollee.setPhoneNumber(enrolleeFromDB.getPhoneNumber());
        }
        if (StringUtils.isEmpty(enrollee.getName())) {
            enrollee.setName(enrolleeFromDB.getName());
        }

        jdbcTemplate.update(SQL_UPDATE_ENROLLEE, enrollee.getName(), enrollee.getActivationStatus(), enrollee.getBirthDate(), enrollee.getPhoneNumber(), enrollee.getId()) ;
        logger.info("Updated Enrollment is {}", enrollee);
        return enrollee;
    }

    @Override
    public Enrollee addEnrollee(Enrollee enrollee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(SQL_CREATE_ENROLLEE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, enrollee.getName());
            ps.setString(2, enrollee.getBirthDate());
            ps.setBoolean(3, enrollee.getActivationStatus());
            ps.setInt(4, enrollee.getPhoneNumber());
            return ps;
        }, keyHolder);
        enrollee.setId(keyHolder.getKey().longValue());
        logger.info("Enrollment Id is {} ", enrollee.getId());
        return enrollee;
    }
    @Override
    public Enrollee getEnrollee(long enrolleeId) {
        List<Enrollee> result = jdbcTemplate.query(SQL_GET_ENROLLEE, new EnrolleeMapper(), enrolleeId);

        if (result == null || result.size() == 0)
            throw new IllegalArgumentException("EnrolleeID not Found");
        logger.info("Enrollee for the enrollment id {} is {}" , enrolleeId, result.get(0));
        Enrollee enrollee = result.get(0);
        List<Dependants> dependants = getDependantsForAnEnrollee(enrollee.getId());
        enrollee.setDependantsList(dependants);
        return enrollee;
    }

    @Override
    @Transactional
    public List<Dependants> addDependants(long enrolleeId, List<Dependants> dependants) {
        validateEnrollee(enrolleeId);
        jdbcTemplate.batchUpdate(SQL_CREATE_DEPENDANT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, dependants.get(i).getEnrollmentId());
                ps.setString(2, dependants.get(i).getName());
                ps.setString(3, dependants.get(i).getBirthDate());
            }
            @Override
            public int getBatchSize() {
                return dependants.size();
            }
        });
        return getDependantsForAnEnrollee(enrolleeId);
    }

    private void validateEnrollee(long enrolleeId) {
        Enrollee enrollee = getEnrollee(enrolleeId);
        if(enrollee==null){
            throw new IllegalArgumentException("EnrolleeID not Found");
        }
    }

    @Override
    public Dependants removeDependants(long enrollmentId, long dependantId) {
        validateEnrollee(enrollmentId);
        Dependants dependants = getDependant(enrollmentId,dependantId);
        jdbcTemplate.update(SQL_DELETE_DEPENDANTS, new Object[]{dependantId,enrollmentId});
        logger.info("Dependant {} with id is {} removed. ",dependants, dependantId);
        return dependants;
    }

    @Override
    public List<Dependants> removeAllDependentsForAnEnrollee(long enrolleeId) {
        validateEnrollee(enrolleeId);
        List<Dependants> dependants = getDependantsForAnEnrollee(enrolleeId);
        jdbcTemplate.update(SQL_DELETE_DEPENDANTS_FOR_ENROLLEE, enrolleeId);
        return dependants;
    }
    @Override
    public Dependants updateDependants(long enrollmentId, Dependants dependants) {
        validateEnrollee(enrollmentId);
        Dependants fromDB = getDependant(enrollmentId,dependants.getId());
        if (dependants.getBirthDate() == null) {
            dependants.setBirthDate(fromDB.getBirthDate());
        }
        if (dependants.getName() == null) {
            dependants.setName(fromDB.getName());
        }
        jdbcTemplate.update(SQL_UPDATE_DEPENDANTS, dependants.getName(), dependants.getBirthDate(), dependants.getId()) ;
        logger.info("Dependant {} with id is {} updated. ",dependants, dependants.getId());
        return dependants;
    }
    @Override
    public List<Dependants> getDependantsForAnEnrollee(long enrolleeId) {
        List<Dependants> dependants =  jdbcTemplate.query(SQL_GET_ALL_DEPENDANT_FOR_ENROLLEE, new DependantMapper(), enrolleeId);
        logger.info("Dependents for the enrollment id {} is {}" , enrolleeId, dependants);
        return dependants;
    }

    @Override
    public Dependants getDependant(long enrollmentId , long dependantId) {
        List<Dependants> dependants =  jdbcTemplate.query(SQL_GET_DEPENDANT, new DependantMapper(), new Object[]{dependantId,enrollmentId});
        if (dependants == null || dependants.size() == 0)
            throw new IllegalArgumentException("Dependent Id not Found");
        logger.info("Dependent created for id {} is {}" , dependantId, dependants);
        return dependants.get(0);
    }
}
