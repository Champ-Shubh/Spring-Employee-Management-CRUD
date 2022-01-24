package com.example.devtraining.repository;

import com.example.devtraining.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT e FROM Employee e WHERE e.email=?1")
    Optional<Employee> findEmployeeByEmail(String email);
}
