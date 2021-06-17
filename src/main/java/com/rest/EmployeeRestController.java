package com.rest;

import com.model.Employee;
import com.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Employee REST controller class
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/employees")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> getOneEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getOneEmployeeById(id), HttpStatus.OK);
    }
}
