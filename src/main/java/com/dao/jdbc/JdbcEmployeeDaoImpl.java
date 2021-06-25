package com.dao.jdbc;

import com.dao.EmployeeDao;
import com.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Employee DAO class implements {@link com.dao.EmployeeDao}
 * and uses {@link org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Repository
public class JdbcEmployeeDaoImpl implements EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcEmployeeDaoImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_SELECT_EMPLOYEE = "SELECT id, full_name, date_of_birth, " +
            "phone_number, email_address, position, date_of_employment, department_id FROM employees WHERE id = :id";

    private final static String SQL_INSERT_EMPLOYEE = "INSERT INTO employees " +
            "(full_name, date_of_birth, phone_number, email_address, position, date_of_employment) " +
            "VALUES (:full_name, :date_of_birth, :phone_number, :email_address, :position, :date_of_employment)";

    private final static String SQL_DELETE_EMPLOYEE = "DELETE FROM employees WHERE id = :id";

    private final static String SQL_INSERT_EMPLOYEE_TO_DEPARTMENT = "UPDATE employees SET department_id = :departmentId " +
            "WHERE id = :employeeId";

    private final static String SQL_DELETE_EMPLOYEE_FROM_DEPARTMENT = "UPDATE employees SET department_id = NULL " +
            "WHERE id = :employeeId";

    private static final String SQL_SELECT_EMPLOYEES_WITHOUT_DEPARTMENT = "SELECT id, full_name, date_of_birth, " +
            "phone_number, email_address, position, date_of_employment, department_id FROM employees WHERE department_id IS NULL";


    @Autowired
    public JdbcEmployeeDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private RowMapper<Employee> rowMapper() {
        LOGGER.debug("rowMapper is running");

        return (rs, i) -> Employee.builder()
                .id(rs.getLong("id"))
                .fullName(rs.getString("full_name"))
                .dateOfBirth(rs.getDate("date_of_birth"))
                .phoneNumber(rs.getString("phone_number"))
                .emailAddress(rs.getString("email_address"))
                .position(rs.getString("position"))
                .dateOfEmployment(rs.getDate("date_of_employment"))
                .departmentId(rs.getLong("department_id"))
                .build();
    }

    @Override
    public Employee getOne(Long id) {
        LOGGER.debug("getOneEmployeeById is running from JdbcEmployeeDaoImpl with id = {}", id);

        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(SQL_SELECT_EMPLOYEE, parameters, rowMapper());
    }

    @Override
    public void createEmployee(Employee employee) {
        LOGGER.debug("createEmployee is running from JdbcEmployeeDaoImpl with employee  full name = {}", employee.getFullName());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("full_name", employee.getFullName());
        parameters.put("date_of_birth", employee.getDateOfBirth());
        parameters.put("phone_number", employee.getPhoneNumber());
        parameters.put("email_address", employee.getEmailAddress());
        parameters.put("position", employee.getPosition());
        parameters.put("date_of_employment", employee.getDateOfEmployment());
        namedParameterJdbcTemplate.update(SQL_INSERT_EMPLOYEE, parameters);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        LOGGER.debug("deleteEmployee is running from JdbcEmployeeDaoImpl with employee  fullName = {}", employee.getFullName());

        SqlParameterSource parameters = new MapSqlParameterSource("id", employee.getId());
        namedParameterJdbcTemplate.update(SQL_DELETE_EMPLOYEE, parameters);
    }

    @Override
    public void addEmployeeToDepartment(Long employeeId, Long departmentId) {
        LOGGER.debug("addEmployeeToDepartment is running from JdbcEmployeeDaoImpl with employeeId = {}, departmentId = {}", employeeId, departmentId);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("employeeId", employeeId);
        parameters.put("departmentId", departmentId);

        namedParameterJdbcTemplate.update(SQL_INSERT_EMPLOYEE_TO_DEPARTMENT, parameters);
    }

    @Override
    public void removeEmployeeFromDepartment(Long employeeId) {
        LOGGER.debug("removeEmployeeFromDepartment is running from JdbcEmployeeDaoImpl with employeeId = {}", employeeId);

        SqlParameterSource parameters = new MapSqlParameterSource("employeeId", employeeId);
        namedParameterJdbcTemplate.update(SQL_DELETE_EMPLOYEE_FROM_DEPARTMENT, parameters);
    }

    @Override
    public List<Employee> getEmployeesByDepartmentIdIsNull() {
        LOGGER.debug("getAllEmployeesWhichDoNotBelongToAnyDepartment is running from JdbcEmployeeDaoImpl");

        return namedParameterJdbcTemplate.query(SQL_SELECT_EMPLOYEES_WITHOUT_DEPARTMENT, rowMapper());
    }
}
