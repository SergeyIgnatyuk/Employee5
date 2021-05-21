package com.service;

import com.dao.EmployeeDao;
import com.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public Employee getOneEmployeeById(Long id) {
        return employeeDao.getOneEmployeeById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createEmployee(Employee employee) {
        employeeDao.createEmployee(employee);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteEmployee(Employee employee) {
        employeeDao.deleteEmployee(employee);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addEmployeeToDepartment(Long employeeId, Long departmentId) {
        employeeDao.addEmployeeToDepartment(employeeId, departmentId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeEmployeeFromDepartment(Long employeeId) {
        employeeDao.removeEmployeeFromDepartment(employeeId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<Employee> getAllEmployeesWhichDoNotBelongToAnyDepartment() {
        return employeeDao.getAllEmployeesWhichDoNotBelongToAnyDepartment();
    }
}
