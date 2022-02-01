package com.example.devtraining.controller;

import com.example.devtraining.model.Employee;
import com.example.devtraining.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        logger.debug("Retrieving list of all employees");

        return employeeService.getEmployees();
    }

    @PostMapping
    public void addEmployee(@RequestBody Employee employee) {
        logger.debug("Initialising new Employee addition");

        employeeService.addEmployee(employee);
        logger.debug("Successfully added new employee");
    }

    @PostMapping(path = "{deptId}")
    public void addEmployeeWithDepartment(@RequestBody Employee employee, @PathVariable("deptId") Long deptId) {
        logger.debug("Initialising new employee creation along with department (id-" + deptId + ")");

        employeeService.addEmployeeWithDepartment(employee, deptId);
        logger.debug("Successfully added employee with department-" + deptId);
    }

    @DeleteMapping(path = "{employeeId}")
    public void removeEmployee(@PathVariable("employeeId") Long employeeId) {
        logger.debug("Initialising deletion of employee-" + employeeId);

        employeeService.deleteEmployee(employeeId);
        logger.debug("Successfully deleted employee-" + employeeId);
    }

    @PutMapping(path = "{employeeId}")
    public void updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) {
        logger.debug("Initialising updation of employee-" + employeeId);

        employeeService.updateEmployee(employeeId, employee);
        logger.debug("Successfully updated employee-" + employeeId);
    }

    @PutMapping(path = "{employeeId}/assign")
    public void assignDepartment(
            @PathVariable("employeeId") Long employeeId,
            @RequestParam(name = "department_id") Long departmentId
    ) {
        logger.debug("Assigning employee-" + employeeId + " to department-" + departmentId);

        employeeService.assignDepartment(employeeId, departmentId);
        logger.debug("Successfully assigned employee-" + employeeId + " to department-" + departmentId);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestBody MultipartFile file) {
        logger.debug("Initialising file upload");

        if (Objects.equals(file.getContentType(), "text/csv")) {
            logger.debug("Converting csv file data to list of employees");
            List<Employee> employees = employeeService.csvToEmployees(file);

            //Take groups of 10-10 records from the list and save to DB
            for (int i = 0; i < employees.size(); i += 10) {
                List<Employee> list = employees.subList(i, Math.min(employees.size(), i + 10));
                employeeService.saveFile(list);
            }
        }
        else {
            logger.error("Terminating file upload - File format not supported");
            throw new IllegalArgumentException("File isn't in CSV format");
        }
    }
}
