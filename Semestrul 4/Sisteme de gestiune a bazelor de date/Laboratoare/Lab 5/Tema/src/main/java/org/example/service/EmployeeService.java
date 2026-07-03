package org.example.service;

import org.example.entities.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Employee create(Employee e, String username) {
        e.setCreatedAt(LocalDateTime.now());
        e.setCreatedBy(username);
        return repo.save(e);
    }

    @Transactional
    public Employee update(Integer id, java.util.function.Consumer<Employee> changes, String username) {
        Employee e = repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        changes.accept(e);
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(username);
        try {
            return repo.save(e);
        } catch (OptimisticLockingFailureException ex) {
            throw new RuntimeException("Conflict detected during update (optimistic locking). Reload the record and retry.", ex);
        }
    }

    @Transactional
    public void softDelete(Integer id, String username) {
        Employee e = repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        e.softDelete(username);
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(username);
        repo.save(e);
    }

    @Transactional
    public void restore(Integer id, String username) {
        Employee e = repo.findByIdIncludingDeleted(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        e.setIsDeleted(false);
        e.setDeletedAt(null);
        e.setDeletedBy(null);
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(username);
        repo.save(e);
    }

    public List<Employee> listAll() {
        return repo.findAll();
    }

    public List<Employee> listDeleted() {
        return repo.findAllByIsDeletedTrue();
    }

    @Transactional
    public void hardDelete(Integer id) {
        Employee e = repo.findByIdIncludingDeleted(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        repo.delete(e);
    }

    public void simulateOptimisticConflict(Integer id) {
        Employee e1 = repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        Employee e2 = repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));

        e1.setEmail(e1.getEmail() + ".a");
        repo.save(e1);

        try {
            e2.setEmail(e2.getEmail() + ".b");
            repo.save(e2);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}

