package com.service;

import com.dao.EmployeeDao;
import com.exceptions.ResourceNotFoundException;
import com.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Employee service class implements {@link com.service.EmployeeService}
 * and calls methods of {@link com.dao.EmployeeDao}.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public Employee getOneEmployeeById(Long id) {
        LOGGER.debug("getOneEmployeeById is running from EmployeeServiceImpl with id = {}", id);

        Employee employee = employeeDao.getOne(id);

        if (employee == null) throw new ResourceNotFoundException("Employee with ID: " + id + " Not Found!");

        return employee;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createEmployee(Employee employee) {
        LOGGER.debug("createEmployee is running from EmployeeServiceImpl with employee full name = {}", employee.getFullName());

        employeeDao.createEmployee(employee);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteEmployee(Employee employee) {
        LOGGER.debug("deleteEmployee is running from EmployeeServiceImpl with employee full name = {}", employee.getFullName());

        employeeDao.deleteEmployee(employee);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addEmployeeToDepartment(Long employeeId, Long departmentId) {
        LOGGER.debug("addEmployeeToDepartment is running from EmployeeServiceImpl with employeeId = {} and departmentId = {}", employeeId, departmentId);

        employeeDao.addEmployeeToDepartment(employeeId, departmentId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeEmployeeFromDepartment(Long employeeId) {
        LOGGER.debug("removeEmployeeFromDepartment is running from EmployeeServiceImpl with employeeId = {}", employeeId);

        employeeDao.removeEmployeeFromDepartment(employeeId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<Employee> getAllEmployeesWhichDoNotBelongToAnyDepartment() {
        LOGGER.debug("getAllEmployeesWhichDoNotBelongToAnyDepartment is running from EmployeeServiceImpl");

        return employeeDao.getEmployeesByDepartmentIdIsNull();
    }
}
