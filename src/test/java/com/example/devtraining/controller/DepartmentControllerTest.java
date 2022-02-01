package com.example.devtraining.controller;

import com.example.devtraining.model.Department;
import com.example.devtraining.model.Employee;
import com.example.devtraining.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldFetchAllDepartments() throws Exception {
        Department dept1 = new Department(
                "testDepartment1",
                "test-code1"
        );
        Department dept2 = new Department(
                "testDepartment2",
                "test-code2"
        );

        List<Department> departments = Arrays.asList(dept1, dept2);

        Mockito.when(departmentService.getDepartments()).thenReturn(departments);

        mvc.perform(get("/api/v1/department")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].departmentName", is(dept1.getDepartmentName())));
    }

    @Test
    void itShouldGetDepartmentById() throws Exception {
        Department dept = new Department(
                "testDepartment",
                "test-code"
        );

        Mockito.when(departmentService.getDepartmentById(any())).thenReturn(dept);

        mvc.perform(get("/api/v1/department/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName", is(dept.getDepartmentName())))
                .andExpect(jsonPath("$.departmentCode", is(dept.getDepartmentCode())));
    }

    @Test
    void itShouldGetEmployeesInDepartment() throws Exception {
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
        Set<Employee> set = new HashSet<>(list);

        Mockito.when(departmentService.getEmployeesInDepartment(any())).thenReturn(set);

        mvc.perform(get("/api/v1/department/1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void itShouldAddNewDepartment() throws Exception {
        Department dept = new Department(
                "testDepartment",
                "test-code"
        );

        Mockito.doNothing().when(departmentService).addDepartment(dept);

        mvc.perform(post("/api/v1/department")
                .content(objectMapper.writeValueAsString(dept))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldDeleteDepartmentById() throws Exception {
        long deptId = 1;

        Mockito.doNothing().when(departmentService).deleteDepartment(deptId);

        mvc.perform(delete("/api/v1/department/" + deptId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldUpdateDepartment() throws Exception {
        Department dept = new Department(
                "testDepartment",
                "test-code"
        );
        long id = 1;

        Mockito.doNothing().when(departmentService).updateDepartment(id, dept);

        mvc.perform(put("/api/v1/department/" + id)
                .content(objectMapper.writeValueAsString(dept))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}