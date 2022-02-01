package com.example.devtraining.service;

import com.example.devtraining.model.Employee;
import com.example.devtraining.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void itShouldFetchAllEmployees() {
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
        List<Employee> employeeList = Arrays.asList(emp1, emp2);

        Mockito.when(employeeRepository.findAll()).thenReturn(employeeList);

        List<Employee> actualList = employeeService.getEmployees();
        assertEquals(employeeList, actualList);
    }

    @Test
    void itShouldThrowErrorWhenAddingEmployeeWithExistingEmail() {
        Employee emp = new Employee(
                "emp",
                "emp@test.com",
                "tester",
                30
        );

        Mockito.when(employeeRepository.findEmployeeByEmail("emp@test.com")).thenReturn(Optional.of(emp));

        assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp));
    }

    @Test
    void itShouldUpdateExistingEmployee() {
        Employee emp = new Employee(
                "emp",
                "emp@test.com",
                "tester",
                30
        );

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee newEmp = new Employee(
                "newEmp",
                "newEmp@test.com",
                "QA engineer",
                25
        );
        employeeService.updateEmployee(1L, newEmp);
        assertEquals(emp.getName(), "newEmp");
        assertEquals(emp.getEmail(), "newEmp@test.com");
        assertEquals(emp.getDesignation(), "QA engineer");
        assertEquals(emp.getAge(), 25);
    }

    @Test
    void itShouldDeleteEmployeeById() {
        long empId = 1L;

        Mockito.when(employeeRepository.existsById(empId)).thenReturn(true);

        employeeService.deleteEmployee(empId);
        Optional<Employee> optional = employeeRepository.findById(empId);
        assertFalse(optional.isPresent());
    }
}