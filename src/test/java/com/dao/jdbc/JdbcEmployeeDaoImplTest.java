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
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

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
    }
}