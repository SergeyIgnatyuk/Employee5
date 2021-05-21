package com.service;

import com.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee getOneEmployeeById(Long id);

    void createEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    void addEmployeeToDepartment(Long employeeId, Long departmentId);

    void removeEmployeeFromDepartment(Long employeeId);

    List<Employee> getAllEmployeesWhichDoNotBelongToAnyDepartment();
}
