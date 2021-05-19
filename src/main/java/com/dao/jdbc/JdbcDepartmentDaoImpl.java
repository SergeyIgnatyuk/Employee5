package com.dao.jdbc;

import com.dao.DepartmentDao;
import com.model.Department;
import com.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JdbcDepartmentDaoImpl implements DepartmentDao {

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
            "LEFT JOIN employees e on d.id = e.department_id WHERE e.id = :id";

    @Autowired
    public JdbcDepartmentDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private ResultSetExtractor<List<Department>> resultSetExtractorForList() {
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
    public List<Department> findAllDepartmentsWithTheirUsers() {
        return namedParameterJdbcTemplate.query(SQL_SELECT_DEPARTMENTS, resultSetExtractorForList());
    }

    @Override
    public Department getOneDepartmentById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.query(SQL_SELECT_DEPARTMENT, parameters, resultSetExtractorForOneDepartment());
    }
}
