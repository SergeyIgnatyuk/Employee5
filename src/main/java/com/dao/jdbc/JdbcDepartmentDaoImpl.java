package com.dao.jdbc;

import com.dao.DepartmentDao;
import com.model.Department;
import com.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

/**
 * Department DAO class implements {@link com.dao.DepartmentDao}
 * and uses {@link org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Repository
public class JdbcDepartmentDaoImpl implements DepartmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDepartmentDaoImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_SELECT_DEPARTMENTS = "SELECT d.id, d.name, " +
            "d.description, d.phone_number, d.date_of_formation, e.id AS employee_id, " +
            "e.full_name, e.date_of_birth, e.phone_number, e.email_address, " +
            "e.position, e.date_of_employment FROM departments d " +
            "LEFT JOIN employees e on d.id = e.department_id";

    private static final String SQL_SELECT_DEPARTMENT = "SELECT d.id, d.name, " +
            "d.description, d.phone_number, d.date_of_formation, e.id AS employee_id, " +
            "e.full_name, e.date_of_birth, e.phone_number, e.email_address, " +
            "e.position, e.date_of_employment FROM departments d " +
            "LEFT JOIN employees e on d.id = e.department_id WHERE d.id = :id";

    private static final String SQL_INSERT_DEPARTMENT = "INSERT INTO departments " +
            "(name, description, phone_number, date_of_formation) VALUES " +
            "(:name, :description, :phone_number, :date_of_formation)";

    private static final String SQL_DELETE_DEPARTMENT = "UPDATE employees SET department_id = null WHERE department_id = :id; " +
            "DELETE FROM departments WHERE id = :id";

    @Autowired
    public JdbcDepartmentDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private ResultSetExtractor<List<Department>> resultSetExtractorForList() {
        LOGGER.debug("resultSetExtractorForList is running");

        return (rs) -> {
            Map<Long, Department> map = new HashMap<>();
            Department department;
            while (rs.next()) {
                Long id = rs.getLong("id");
                department = map.get(id);
                if (department == null) {
                    department = Department.builder()
                            .id(id)
                            .name(rs.getString("name"))
                            .description((rs.getString("description")))
                            .phoneNumber(rs.getString("phone_number"))
                            .dateOfFormation(rs.getDate("date_of_formation"))
                            .build();
                    map.put(id, department);
                }
                Long employeeId = rs.getLong("employee_id");
                if (employeeId > 0) {
                    department.getEmployees().add(Employee.builder()
                            .id(employeeId)
                            .fullName(rs.getString("full_name"))
                            .dateOfBirth(rs.getDate("date_of_birth"))
                            .phoneNumber(rs.getString("phone_number"))
                            .emailAddress(rs.getString("email_address"))
                            .position(rs.getString("position"))
                            .dateOfEmployment(rs.getDate("date_of_employment"))
                            .build());
                }
            }
            return new ArrayList<>(map.values());
        };
    }

    private ResultSetExtractor<Department> resultSetExtractorForOneDepartment() {
        LOGGER.debug("resultSetExtractorForOneDepartment is running from JdbcDepartmentDaoImpl");

        return (rs) -> {
            Department department = new Department();
            Set<Employee> employees = new HashSet<>();

            while (rs.next()) {
                department.setId(rs.getLong("id"));
                department.setName(rs.getString("name"));
                department.setDescription(rs.getString("description"));
                department.setPhoneNumber(rs.getString("phone_number"));
                department.setDateOfFormation(rs.getDate("date_of_formation"));

                employees.add(Employee.builder()
                        .id(rs.getLong("employee_id"))
                        .fullName(rs.getString("full_name"))
                        .dateOfBirth(rs.getDate("date_of_birth"))
                        .phoneNumber(rs.getString("phone_number"))
                        .emailAddress(rs.getString("email_address"))
                        .position(rs.getString("position"))
                        .dateOfEmployment(rs.getDate("date_of_employment"))
                        .build());
            }
            department.getEmployees().addAll(employees);
            return department;
        };
    }

    @Override
    public List<Department> getAllDepartmentsWithTheirUsers() {
        LOGGER.debug("findAllDepartmentsWithTheirUsers is running from JdbcDepartmentDaoImpl");

        return namedParameterJdbcTemplate.query(SQL_SELECT_DEPARTMENTS, resultSetExtractorForList());
    }

    @Override
    public Department getOneDepartmentById(Long id) {
        LOGGER.debug("getOneDepartmentById is running from JdbcDepartmentDaoImpl with id = {}", id);

        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.query(SQL_SELECT_DEPARTMENT, parameters, resultSetExtractorForOneDepartment());
    }

    @Override
    public void createDepartment(Department department) {
        LOGGER.debug("createDepartment is running from JdbcDepartmentDaoImpl with department name = {}", department.getName());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", department.getName());
        parameters.put("description", department.getDescription());
        parameters.put("phone_number", department.getPhoneNumber());
        parameters.put("date_of_formation", department.getDateOfFormation());

        namedParameterJdbcTemplate.update(SQL_INSERT_DEPARTMENT, parameters);
    }

    @Override
    public void deleteDepartment(Department department) {
        LOGGER.debug("deleteDepartment is running from JdbcDepartmentDaoImpl with department name = {}", department.getName());

        SqlParameterSource parameters = new MapSqlParameterSource("id", department.getId());

        namedParameterJdbcTemplate.update(SQL_DELETE_DEPARTMENT, parameters);
    }
}
