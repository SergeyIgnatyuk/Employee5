package com.service;

import com.dao.DepartmentDao;
import com.model.Department;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao;

    public DepartmentServiceImpl(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public List<Department> findAllDepartmentsWithTheirUsers() {
        return departmentDao.findAllDepartmentsWithTheirUsers();
    }

    @Override
    public Department getOneDepartmentById(Long id) {
        return departmentDao.getOneDepartmentById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createDepartment(Department department) {
        departmentDao.createDepartment(department);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteDepartment(Department department) {
        departmentDao.deleteDepartment(department);
    }
}
