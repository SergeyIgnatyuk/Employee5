package com.service;

import com.dao.EmployeeDao;
import com.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmployeeServiceImplTest.EmployeeServiceImplTestConfig.class})
public class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeDao employeeDao;

    @Configuration
    public static class EmployeeServiceImplTestConfig {

        @Bean
        public EmployeeDao employeeDao() {
            return mock(EmployeeDao.class);
        }

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl(employeeDao());
        }
    }

    @Test
    public void getOneEmployeeById() {
        Long id = 1L;
        when(employeeDao.getOne(id)).thenReturn(new Employee());
        employeeService.getOneEmployeeById(id);
        verify(employeeDao, times(1)).getOne(id);
    }

    @Test
    public void createEmployee() {
        Employee employee = new Employee();
        employeeService.createEmployee(employee);
        verify(employeeDao, times(1)).createEmployee(employee);
    }

    @Test
    public void deleteEmployee() {
        Employee employee = new Employee();
        employeeService.deleteEmployee(employee);
        verify(employeeDao, times(1)).deleteEmployee(employee);
    }

    @Test
    public void addEmployeeToDepartment() {
        Long employeeId = 1L;
        Long departmentId = 1L;
        employeeService.addEmployeeToDepartment(employeeId, departmentId);
        verify(employeeDao, times(1)).addEmployeeToDepartment(employeeId, departmentId);
    }

    @Test
    public void removeEmployeeFromDepartment() {
        Long employeeId = 1L;
        employeeService.removeEmployeeFromDepartment(employeeId);
        verify(employeeDao, times(1)).removeEmployeeFromDepartment(employeeId);
    }

    @Test
    public void getAllEmployeesWhichDoNotBelongToAnyDepartment() {
        employeeService.getAllEmployeesWhichDoNotBelongToAnyDepartment();
        verify(employeeDao, times(1)).getEmployeesByDepartmentIdIsNull();
    }
}