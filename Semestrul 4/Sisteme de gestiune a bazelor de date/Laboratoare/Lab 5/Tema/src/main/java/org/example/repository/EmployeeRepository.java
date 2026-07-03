package org.example.repository;

import org.example.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Admin: Get all soft-deleted records
    @Query(value = "SELECT * FROM employees WHERE is_deleted = true", nativeQuery = true)
    List<Employee> findAllByIsDeletedTrue();
    
    // Admin: Get any employee by id, including soft-deleted
    @Query(value = "SELECT * FROM employees WHERE id = :id", nativeQuery = true)
    Optional<Employee> findByIdIncludingDeleted(@Param("id") Integer id);
}

