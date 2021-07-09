package com.rest;

import com.model.Employee;
import com.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmployeeRestControllerTest.EmployeeRestControllerTestConfig.class})
public class EmployeeRestControllerTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Configuration
    @EnableWebMvc
    public static class EmployeeRestControllerTestConfig {

        @Bean
        public EmployeeService employeeService() {
            return mock(EmployeeService.class);
        }

        @Bean
        public EmployeeRestController employeeRestController() {
            return new EmployeeRestController(employeeService());
        }
    }

    @Test
    //    @WithMockUser(roles = {"ROLE_ADMIN, ROLE_USER"})
    public void givenEmployee_whenGetOneEmployeeById_thenReturnJson() throws Exception {
        Long id = 1L;
        Employee employee = Employee.builder()
                .id(id)
                .fullName("full name")
                .dateOfBirth(new Date())
                .phoneNumber("phone number")
                .emailAddress("email address")
                .position("position")
                .dateOfEmployment(new Date())
                .build();

        when(employeeService.getOneEmployeeById(id)).thenReturn(employee);

        this.mockMvc.perform(get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
                .andExpect(jsonPath("$.fullName").value(employee.getFullName()))
                .andExpect(jsonPath("$.dateOfBirth").value(new SimpleDateFormat("yyyy-MM-dd")
                        .format(employee.getDateOfBirth())))
                .andExpect(jsonPath("$.phoneNumber").value(employee.getPhoneNumber()))
                .andExpect(jsonPath("$.emailAddress").value(employee.getEmailAddress()))
                .andExpect(jsonPath("$.position").value(employee.getPosition()))
                .andExpect(jsonPath("$.dateOfEmployment").value(new SimpleDateFormat("yyyy-MM-dd")
                        .format(employee.getDateOfEmployment())));
    }
}