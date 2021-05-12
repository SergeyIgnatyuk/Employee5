package com.dao.jdbc;

import com.dao.DepartmentDao;
import com.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcDepartmentDaoImpl implements DepartmentDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_SELECT_DEPARTMENTS = "SELECT id, name, " +
            "description, phone_number, date_of_formation FROM departments";

    @Autowired
    public JdbcDepartmentDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Department> findAll() {
        return null;
    }
}
