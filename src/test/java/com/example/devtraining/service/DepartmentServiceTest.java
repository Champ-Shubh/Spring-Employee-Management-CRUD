package com.example.devtraining.service;

import com.example.devtraining.model.Department;
import com.example.devtraining.model.Employee;
import com.example.devtraining.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    DepartmentRepository repository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void itShouldFetchAllDepartments() {
        Department dept1 = new Department(
                "testDepartment1",
                "test-code1"
        );
        Department dept2 = new Department(
                "testDepartment2",
                "test-code2"
        );
        List<Department> expectedList = Arrays.asList(dept1, dept2);

        Mockito.when(repository.findAll()).thenReturn(expectedList);

        List<Department> actualList = departmentService.getDepartments();
        assertEquals(actualList, expectedList);
    }

    @Test
    void itShouldGetDepartmentById() {
        Department dept = new Department(
                "testDepartment",
                "test-code"
        );

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(dept));

        Department actualDept = departmentService.getDepartmentById(1L);
        assertEquals(actualDept, dept);
    }

    @Test
    void getEmployeesInDepartment() {
        Department dept = new Department();
        Employee emp1 = new Employee(
                "emp1",
                "emp1@test.com",
                "tester",
                30
        );
        Employee emp2 = new Employee(
                "emp2",
                "emp2@test.com",
                "tester",
                25
        );

        List<Employee> list = Arrays.asList(emp1, emp2);
        dept.setEmployees(new HashSet<>(list));

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(dept));

        Set<Employee> employeeSet = departmentService.getEmployeesInDepartment(1L);
        assertEquals(employeeSet.size(), 2);
        assertTrue(employeeSet.contains(emp1));
        assertTrue(employeeSet.contains(emp2));
    }

    @Test
    void itShouldDeleteDepartment() {
        long deptId = 1L;

        Mockito.when(repository.existsById(deptId)).thenReturn(true);

        departmentService.deleteDepartment(deptId);
        Optional<Department> optional = repository.findById(deptId);
        assertFalse(optional.isPresent());
    }

    @Test
    void itShouldUpdateDepartment() {
        Department dept = new Department(
                "testDepartment",
                "test-code"
        );

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(dept));

        Department newDept = new Department(
                "newDeptName",
                "newDept-Code"
        );
        departmentService.updateDepartment(1L, newDept);

        assertEquals(dept.getDepartmentName(), "newDeptName");
        assertEquals(dept.getDepartmentCode(), "newDept-Code");
    }
}