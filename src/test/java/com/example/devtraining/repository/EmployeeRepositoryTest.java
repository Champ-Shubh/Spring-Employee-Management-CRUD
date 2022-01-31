package com.example.devtraining.repository;

import com.example.devtraining.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void itShouldFindEmployeeByEmail() {
        //given
        String testEmail = "test-email@gmail.com";
        Employee dummyEmployee = new Employee(
                "Test",
                testEmail,
                "Manager",
                40
        );
        employeeRepository.save(dummyEmployee);

        //when
        Optional<Employee> emp = employeeRepository.findEmployeeByEmail(testEmail);

        //then
        Assertions.assertThat(emp).isPresent();
    }
}