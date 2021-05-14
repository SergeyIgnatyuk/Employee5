package com.dao.jdbc;

import com.dao.EmployeeDao;
import com.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcEmployeeDaoImpl implements EmployeeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_SELECT_EMPLOYEE = "SELECT id, full_name, " +
            "date_of_birth, phone_number, email_address, position, date_of_employment " +
            "FROM employees WHERE id = :id";

    @Autowired
    public JdbcEmployeeDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private RowMapper<Employee> rowMapper() {
        return (rs, i) -> Employee.builder()
                .id(rs.getLong("id"))
                .fullName(rs.getString("full_name)"))
                .dateOfBirth(rs.getDate("date_of_birth"))
                .phoneNumber(rs.getString("phone_number"))
                .emailAddress(rs.getString("email_address"))
                .position(rs.getString("position"))
                .dateOfEmployment(rs.getDate("date_of_employment"))
                .build();
    }

    @Override
    public Employee getOneEmployeeById(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.queryForObject(SQL_SELECT_EMPLOYEE, params, rowMapper());
    }
}
