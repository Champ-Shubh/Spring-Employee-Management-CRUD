package com.example.devtraining.repository;

import com.example.devtraining.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /*
    Uncomment this in case you want to add UNIQUE constraint to departmentCode column in Department

    @Query(value = "SELECT * FROM Department d WHERE d.department_code = :deptCode", nativeQuery = true)
    Optional<Department> findDepartmentByDepartmentCode(String deptCode);
     */
}
