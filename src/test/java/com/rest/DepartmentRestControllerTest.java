package com.rest;

import com.model.Department;
import com.model.Employee;
import com.service.DepartmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DepartmentRestControllerTest.DepartmentRestControllerTestConfig.class})
public class DepartmentRestControllerTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Configuration
    @EnableWebMvc
    public static class DepartmentRestControllerTestConfig {

        @Bean
        public DepartmentService departmentService() {
            return mock(DepartmentService.class);
        }

        @Bean
        public DepartmentRestController departmentRestController() {
            return new DepartmentRestController(departmentService());
        }
    }

    @Test
//    @WithMockUser(roles = {"ROLE_ADMIN, ROLE_USER"})
    public void givenDepartments_whenGetAllDepartmentsWithTheirUsers_thenReturnJson() throws Exception {
        List<Department> departmentList = Stream.of(Department.builder()
                .id(1L)
                .name("first department")
                .description("description")
                .phoneNumber("phone number")
                .dateOfFormation(new Date())
                .employees(Stream.of(Employee.builder()
                        .id(1L)
                        .fullName("full name")
                        .dateOfBirth(new Date())
                        .phoneNumber("phone number")
                        .emailAddress("email address")
                        .position("position")
                        .dateOfEmployment(new Date())
                        .build()).collect(Collectors.toSet()))
                .build()).collect(Collectors.toList());

        Department departmentFromList = departmentList.get(0);

        Employee firstEmployeeFromDepartment = departmentFromList.getEmployees().stream().findFirst().get();

        when(departmentService.getAllDepartmentsWithTheirUsers()).thenReturn(departmentList);

        this.mockMvc.perform(get("/departments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(departmentFromList.getId().intValue()))
                .andExpect(jsonPath("$[0].name").value(departmentFromList.getName()))
                .andExpect(jsonPath("$[0].description").value(departmentFromList.getDescription()))
                .andExpect(jsonPath("$[0].phoneNumber").value(departmentFromList.getPhoneNumber()))
                .andExpect(jsonPath("$[0].dateOfFormation").value(new SimpleDateFormat("yyyy-MM-dd").format(departmentFromList.getDateOfFormation())))
                .andExpect(jsonPath("$[0].employees[0].id").value(firstEmployeeFromDepartment.getId().intValue()))
                .andExpect(jsonPath("$[0].employees[0].fullName").value(firstEmployeeFromDepartment.getFullName()))
                .andExpect(jsonPath("$[0].employees[0].dateOfBirth").value(new SimpleDateFormat("yyyy-MM-dd").format(firstEmployeeFromDepartment.getDateOfBirth())))
                .andExpect(jsonPath("$[0].employees[0].phoneNumber").value(firstEmployeeFromDepartment.getPhoneNumber()))
                .andExpect(jsonPath("$[0].employees[0].emailAddress").value(firstEmployeeFromDepartment.getEmailAddress()))
                .andExpect(jsonPath("$[0].employees[0].position").value(firstEmployeeFromDepartment.getPosition()))
                .andExpect(jsonPath("$[0].employees[0].dateOfEmployment").value(new SimpleDateFormat("yyyy-MM-dd").format(firstEmployeeFromDepartment.getDateOfEmployment())));
    }

    @Test
//    @WithMockUser(roles = {"ROLE_ADMIN, ROLE_USER"})
    public void givenDepartment_whenGetOneDepartment_thenReturnJson() throws Exception {
        Department department = Department.builder()
                .id(1L)
                .name("first department")
                .description("description")
                .phoneNumber("phone number")
                .dateOfFormation(new Date())
                .employees(Stream.of(Employee.builder()
                        .id(1L)
                        .fullName("full name")
                        .dateOfBirth(new Date())
                        .phoneNumber("phone number")
                        .emailAddress("email address")
                        .position("position")
                        .dateOfEmployment(new Date())
                        .build()).collect(Collectors.toSet()))
                .build();

        Employee employee = department.getEmployees().stream().findFirst().get();

        when(departmentService.getOneDepartmentById(1L)).thenReturn(department);

        this.mockMvc.perform(get("/departments/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(department.getId().intValue()))
                .andExpect(jsonPath("$.name").value(department.getName()))
                .andExpect(jsonPath("$.description").value(department.getDescription()))
                .andExpect(jsonPath("$.phoneNumber").value(department.getPhoneNumber()))
                .andExpect(jsonPath("$.dateOfFormation").value(new SimpleDateFormat("yyyy-MM-dd").format(department.getDateOfFormation())))
                .andExpect(jsonPath("$.employees[0].id").value(employee.getId().intValue()))
                .andExpect(jsonPath("$.employees[0].fullName").value(employee.getFullName()))
                .andExpect(jsonPath("$.employees[0].dateOfBirth").value(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateOfBirth())))
                .andExpect(jsonPath("$.employees[0].phoneNumber").value(employee.getPhoneNumber()))
                .andExpect(jsonPath("$.employees[0].emailAddress").value(employee.getEmailAddress()))
                .andExpect(jsonPath("$.employees[0].position").value(employee.getPosition()))
                .andExpect(jsonPath("$.employees[0].dateOfEmployment").value(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDateOfEmployment())));
    }
}