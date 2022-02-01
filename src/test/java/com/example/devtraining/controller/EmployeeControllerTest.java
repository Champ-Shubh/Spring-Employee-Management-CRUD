package com.example.devtraining.controller;

import com.example.devtraining.model.Employee;
import com.example.devtraining.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    //TODO: Write some failing tests too for validation checks

    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldFetchAllEmployees() throws Exception {
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

        Mockito.when(employeeService.getEmployees()).thenReturn(list);

        mvc.perform(get("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(emp1.getName())))
                .andExpect(jsonPath("$[0].email", is(emp1.getEmail())))
                .andExpect(jsonPath("$[0].designation", is(emp1.getDesignation())))
                .andExpect(jsonPath("$[0].age", is(emp1.getAge())));
    }

    @Test
    void itShouldAddNewEmployee() throws Exception {
        Employee emp = new Employee(
                "emp",
                "emp@test.com",
                "tester",
                30
        );

        Mockito.doNothing().when(employeeService).addEmployee(emp);

        mvc.perform(post("/api/v1/employee")
                .content(objectMapper.writeValueAsString(emp))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldAddEmployeeWithDepartment() throws Exception {
        Employee emp = new Employee(
                "emp",
                "emp@test.com",
                "tester",
                30
        );
        long deptId = 1;

        Mockito.doNothing().when(employeeService).addEmployeeWithDepartment(emp, deptId);

        mvc.perform(post("/api/v1/employee/" + deptId)
                .content(objectMapper.writeValueAsString(emp))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldRemoveExistingEmployee() throws Exception {
        long empId = 1;

        Mockito.doNothing().when(employeeService).deleteEmployee(empId);

        mvc.perform(delete("/api/v1/employee/" + empId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldUpdateEmployee() throws Exception {
        Employee emp = new Employee(
                "emp",
                "emp@test.com",
                "tester",
                30
        );
        long empId = 1;

        Mockito.doNothing().when(employeeService).updateEmployee(empId, emp);

        mvc.perform(put("/api/v1/employee/" + empId)
                .content(objectMapper.writeValueAsString(emp))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldAssignDepartmentToExistingEmployee() throws Exception {
        long empId = 1;
        long deptId = 1;

        Mockito.doNothing().when(employeeService).assignDepartment(empId, deptId);

        mvc.perform(put("/api/v1/employee/" + empId + "/assign")
                .param("department_id", String.valueOf(deptId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}