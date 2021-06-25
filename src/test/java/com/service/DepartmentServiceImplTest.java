package com.service;

import com.dao.DepartmentDao;
import com.model.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DepartmentServiceImplTest.DepartmentServiceImplTestConfig.class})
public class DepartmentServiceImplTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentDao departmentDao;

    @Configuration
    public static class DepartmentServiceImplTestConfig {

        @Bean
        public DepartmentDao departmentDao() {
            return mock(DepartmentDao.class);
        }

        @Bean
        public DepartmentService departmentService() {
            return new DepartmentServiceImpl(departmentDao());
        }
    }

    @Test
    public void findAllDepartmentsWithTheirUsers() {
        departmentService.getAllDepartmentsWithTheirUsers();
        verify(departmentDao, times(1)).findAll();
    }

    @Test
    public void getOneDepartmentById() {
        Long id = 1L;
        when(departmentDao.getOne(id)).thenReturn(new Department());
        departmentService.getOneDepartmentById(id);
        verify(departmentDao, times(1)).getOne(id);
    }

    @Test
    public void createDepartment() {
        Department department = new Department();
        departmentService.createDepartment(department);
        verify(departmentDao, times(1)).createDepartment(department);
    }

    @Test
    public void deleteDepartment() {
        Department department = new Department();
        departmentService.deleteDepartment(department);
        verify(departmentDao, times(1)).deleteDepartment(department);
    }
}