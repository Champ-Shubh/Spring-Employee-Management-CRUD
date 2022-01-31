package com.example.devtraining.controller;

import com.example.devtraining.model.Department;
import com.example.devtraining.model.Employee;
import com.example.devtraining.service.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final Logger logger = LogManager.getLogger(DepartmentController.class);

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        logger.debug("Retrieving list of all departments");
        return departmentService.getDepartments();
    }

    //TODO: Update README file for this new endpoint
    @GetMapping(path = "{deptId}")
    public Department getDepartmentById(@PathVariable("deptId") Long deptId){
        logger.debug("Retrieving department-" + deptId);
        return departmentService.getDepartmentById(deptId);
    }

    @GetMapping(path = "{deptId}/employees")
    public List<Employee> getEmployeesInDepartment(@PathVariable("deptId") Long deptId) {
        logger.debug("Retrieving list of all employees in department-" + deptId);
        return new ArrayList<>(departmentService.getEmployeesInDepartment(deptId));
    }

    @PostMapping
    public void addDepartment(@RequestBody Department department) {
        logger.debug("Initialising addition of new department");

        departmentService.addDepartment(department);
        logger.debug("Successfully added new department");
    }

    @DeleteMapping(path = "{deptId}")
    public void deleteDepartment(@PathVariable("deptId") Long deptId) {
        logger.debug("Initialising deletion of department-" + deptId);

        departmentService.deleteDepartment(deptId);
        logger.debug("Successfully deleted department-" + deptId);
    }

    @PutMapping(path = "{deptId}")
    public void updateDepartment(@PathVariable("deptId") Long deptId, @RequestBody Department department) {
        logger.debug("Initialising updation of department-" + deptId);

        departmentService.updateDepartment(deptId, department);
        logger.debug("Successfully updated department-" + deptId);
    }
}
