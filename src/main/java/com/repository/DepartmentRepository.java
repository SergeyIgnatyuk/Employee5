package com.repository;

import com.dao.DepartmentDao;
import com.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for {@link com.model.Department}
 * Implementation of {@link JpaRepository} interface.
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentDao {
}
