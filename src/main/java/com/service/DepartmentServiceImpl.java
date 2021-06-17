package com.service;

import com.dao.DepartmentDao;
import com.model.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Department service class implements {@link com.service.DepartmentService}
 * and calls methods of {@link com.dao.DepartmentDao}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentDao departmentDao;

    public DepartmentServiceImpl(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public List<Department> getAllDepartmentsWithTheirUsers() {
        LOGGER.debug("getAllDepartmentsWithTheirUsers is running from DepartmentServiceImpl");

        return departmentDao.getAllDepartmentsWithTheirUsers();
    }

    @Override
    public Department getOneDepartmentById(Long id) {
        LOGGER.debug("getOneDepartmentById is running from DepartmentServiceImpl with id = {}", id);

        return departmentDao.getOneDepartmentById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createDepartment(Department department) {
        LOGGER.debug("createDepartment is running from DepartmentServiceImpl with department name = {}", department.getName());

        departmentDao.createDepartment(department);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteDepartment(Department department) {
        LOGGER.debug("deleteDepartment is running from DepartmentServiceImpl with department name = {}", department.getName());

        departmentDao.deleteDepartment(department);
    }
}
