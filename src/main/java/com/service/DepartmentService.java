package com.service;

import com.model.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getAllDepartmentsWithTheirUsers();

    Department getOneDepartmentById(Long id);

    void createDepartment(Department department);

    void deleteDepartment(Department department);
}
