package com.dao.jdbc;


import com.dao.DepartmentDao;
import com.model.Department;
import com.model.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcDepartmentDaoImplTest.JdbcDepartmentDaoImplTestConfig.class)
@Transactional
public class JdbcDepartmentDaoImplTest {

    @Autowired
    private DepartmentDao departmentDao;

    @Configuration
    @EnableTransactionManagement
    @ComponentScan(basePackages = "com.dao.jdbc")
    public static class JdbcDepartmentDaoImplTestConfig {
        @Bean
        public DataSource dataSource() {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            return builder.setType(EmbeddedDatabaseType.H2).addScript("classpath:h2.sql").build();
        }

        @Bean
        public TransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }
    }

    @Test
    @Rollback(value = true)
    public void findAllDepartmentsWithTheirUsersTest() {
        List<Department> departmentList = departmentDao.findAllDepartmentsWithTheirUsers();

        Department firstDepartmentFromList = departmentList.get(0);

        Set<Employee> employeesFromFirstDepartment = firstDepartmentFromList.getEmployees();

        Department secondDepartmentFromList = departmentList.get(1);

        Set<Employee> employeesFromSecondDepartment = secondDepartmentFromList.getEmployees();

        Assert.assertEquals(2, departmentList.size());

        Assert.assertEquals("Logistics Department", firstDepartmentFromList.getName());
        Assert.assertEquals("logistics, expedition, planning", firstDepartmentFromList.getDescription());
        Assert.assertEquals("+375336446244", firstDepartmentFromList.getPhoneNumber());
        Assert.assertEquals(new GregorianCalendar(2008, Calendar.MARCH, 15).getTime(), firstDepartmentFromList.getDateOfFormation());
        Assert.assertEquals(2 , employeesFromFirstDepartment.size());

        Assert.assertEquals("Transport Service", secondDepartmentFromList.getName());
        Assert.assertEquals("tire fitting, car wash, drivers", secondDepartmentFromList.getDescription());
        Assert.assertEquals("+375336333140", secondDepartmentFromList.getPhoneNumber());
        Assert.assertEquals(new GregorianCalendar(2020, Calendar.SEPTEMBER, 1).getTime(), secondDepartmentFromList.getDateOfFormation());
        Assert.assertEquals(0 , employeesFromSecondDepartment.size());
    }

    @Test
    @Rollback(value = true)
    public void getOneDepartmentByIdTest() {
        Department department = departmentDao.getOneDepartmentById(1L);

        Assert.assertEquals("Logistics Department", department.getName());
        Assert.assertEquals("logistics, expedition, planning", department.getDescription());
        Assert.assertEquals("+375336446244", department.getPhoneNumber());
        Assert.assertEquals(new GregorianCalendar(2008, Calendar.MARCH, 15).getTime(), department.getDateOfFormation());

        Assert.assertEquals(2, department.getEmployees().size());

    }

    @Test
    @Rollback(value = true)
    public void createDepartmentTest() {
        Long id = 3L;

        departmentDao.createDepartment(Department.builder()
        .id(id)
        .name("department")
        .description("description")
        .phoneNumber("phone number")
        .dateOfFormation(new Date())
        .build());

        Department department = departmentDao.getOneDepartmentById(id);

        Assert.assertEquals(id, department.getId());
        Assert.assertEquals("department", department.getName());
        Assert.assertEquals("description", department.getDescription());
        Assert.assertEquals("phone number", department.getPhoneNumber());
    }

    @Test
    @Rollback(value = true)
    public void deleteDepartmentTest() {
        Long id = 1L;
        Department department = departmentDao.getOneDepartmentById(id);

        departmentDao.deleteDepartment(department);

        List<Department> departmentList = departmentDao.findAllDepartmentsWithTheirUsers();

        Assert.assertEquals(1, departmentList.size());
    }
}
