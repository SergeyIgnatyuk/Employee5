package com.dao.jdbc;

import com.dao.DepartmentDao;
import com.model.Department;
import com.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcDepartmentDaoImpl implements DepartmentDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_SELECT_DEPARTMENTS = "SELECT d.id, d.name, " +
            "d.description, d.phone_number, d.date_of_formation, e.id AS employee_id, " +
            "e.full_name, e.date_of_birth, e.phone_number, e.email_address, " +
            "e.position, e.date_of_employment FROM departments d " +
            "LEFT JOIN employees e on d.id = e.department_id";

    @Autowired
    public JdbcDepartmentDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private ResultSetExtractor<List<Department>> resultSetExtractor() {
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

    @Override
    public List<Department> findAllDepartmentsWithTheirUsers() {
        return namedParameterJdbcTemplate.query(SQL_SELECT_DEPARTMENTS, resultSetExtractor());
    }
}
