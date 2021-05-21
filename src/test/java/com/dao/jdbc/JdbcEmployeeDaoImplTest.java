package com.dao.jdbc;

import com.dao.EmployeeDao;
import com.model.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JdbcDepartmentDaoImplTest.JdbcDepartmentDaoImplTestConfig.class})
@Transactional
public class JdbcEmployeeDaoImplTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Configuration
    @EnableTransactionManagement
    @ComponentScan(basePackages = {"com.dao.jdbc"})
    public static class JdbcEmployeeDaoImplTestConfig {
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
    public void getOneEmployeeByIdTest() {

        Employee employee = employeeDao.getOneEmployeeById(1L);

        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals("Sergey Ignatuk", employee.getFullName());
        Assert.assertEquals(new GregorianCalendar(1989, Calendar.AUGUST, 7).getTime(), employee.getDateOfBirth());
        Assert.assertEquals("+375295326531", employee.getPhoneNumber());
        Assert.assertEquals("s.ignatyuk@yuridan.by", employee.getEmailAddress());
        Assert.assertEquals("scheduler", employee.getPosition());
        Assert.assertEquals(new GregorianCalendar(2012, Calendar.NOVEMBER, 14).getTime(), employee.getDateOfEmployment());
        Assert.assertEquals(1, employee.getDepartmentId().intValue());
    }

    @Test
    @Rollback(value = true)
    public void createEmployeeTest() {
        Long id = 4L;
        employeeDao.createEmployee(Employee.builder()
                .id(id)
                .fullName("full name")
                .dateOfBirth(new Date())
                .phoneNumber("phone number")
                .emailAddress("email address")
                .position("position")
                .dateOfEmployment(new Date())
                .build());

        Employee employee = employeeDao.getOneEmployeeById(id);

        Assert.assertEquals(4, employee.getId().intValue());
        Assert.assertEquals("full name", employee.getFullName());
        Assert.assertEquals("phone number", employee.getPhoneNumber());
        Assert.assertEquals("email address", employee.getEmailAddress());
        Assert.assertEquals("position", employee.getPosition());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(value = true)
    public void deleteEmployeeTest() {
        Long id = 1L;
        Employee employee = employeeDao.getOneEmployeeById(id);

        employeeDao.deleteEmployee(employee);

        employee = employeeDao.getOneEmployeeById(id);
    }

    @Test
    @Rollback(value = true)
    public void addEmployeeToDepartmentTest() {
        Long employeeId = 1L;
        Employee employee = employeeDao.getOneEmployeeById(employeeId);

        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals(1, employee.getDepartmentId().intValue());

        employeeDao.addEmployeeToDepartment(employeeId, 2L);

        employee = employeeDao.getOneEmployeeById(employeeId);

        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals(2, employee.getDepartmentId().intValue());
    }

    @Test
    @Rollback(value = true)
    public void removeEmployeeFromDepartmentTest() {
        Long employeeId = 1L;
        Employee employee = employeeDao.getOneEmployeeById(employeeId);

        Assert.assertEquals(1, employee.getId().intValue());
        Assert.assertEquals(1, employee.getDepartmentId().intValue());

        employeeDao.removeEmployeeFromDepartment(employeeId);

        employee = employeeDao.getOneEmployeeById(employeeId);

        Assert.assertEquals(0, employee.getDepartmentId().intValue());
    }

    @Test
    @Rollback(value = true)
    public void getAllEmployeesWhichDoNotBelongToAnyDepartmentTest() {
        List<Employee> employees = employeeDao.getAllEmployeesWhichDoNotBelongToAnyDepartment();

        Assert.assertEquals(1, employees.size());

        Employee employee = employees.get(0);

        Assert.assertEquals(3, employee.getId().intValue());
        Assert.assertEquals("Ivanovskaya Yulia", employee.getFullName());
        Assert.assertEquals(new GregorianCalendar(1988, Calendar.JULY, 31).getTime(), employee.getDateOfBirth());
        Assert.assertEquals("+375295202152", employee.getPhoneNumber());
        Assert.assertEquals("info@yuridan.by", employee.getEmailAddress());
        Assert.assertEquals("forwarder", employee.getPosition());
        Assert.assertEquals(new GregorianCalendar(2014, Calendar.OCTOBER, 31).getTime(), employee.getDateOfEmployment());
    }
}