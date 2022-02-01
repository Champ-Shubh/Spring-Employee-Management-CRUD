package com.example.devtraining.service;

import com.example.devtraining.model.Department;
import com.example.devtraining.model.Employee;
import com.example.devtraining.repository.DepartmentRepository;
import com.example.devtraining.repository.EmployeeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    private final Logger LOGGER = LogManager.getLogger(EmployeeService.class);

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    //Add a new employee if email is not already taken
    public void addEmployee(Employee employee) {
        Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmail(employee.getEmail());

        if (employeeByEmail.isPresent()) {
            LOGGER.error("Email already exists - Terminating employee creation");
            throw new IllegalArgumentException("Email already taken, choose a different one");
        }

        employeeRepository.save(employee);
    }

    //Add a new employee with department set to department with id deptId
    public void addEmployeeWithDepartment(Employee employee, Long deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + deptId + ") does not exist"));

        LOGGER.debug("Department exists, proceeding with assignment");

        employee.setDepartment(department);
        LOGGER.debug("Department assigned to employee");

        addEmployee(employee);
    }

    //Perform validation and update employee
    @Transactional
    public void updateEmployee(Long id, Employee newEmployee) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee (id - " + id + ") does not exist"));

        LOGGER.debug("Employee does exist, proceeding with updation");

        String newName = newEmployee.getName();
        String newDesignation = newEmployee.getDesignation();
        String newEmail = newEmployee.getEmail();
        Integer newAge = newEmployee.getAge();

        if (newName != null && newName.length() > 0 && !Objects.equals(newName, employee.getName())) {
            employee.setName(newName);
            LOGGER.debug("Updating employee-" + id + ", name updated");
        }

        if (newDesignation != null && newDesignation.length() > 0 && !Objects.equals(newDesignation, employee.getDesignation())) {
            employee.setDesignation(newDesignation);
            LOGGER.debug("Updating employee-" + id + ", designation updated");
        }

        if (newAge != null && newAge >= 0 && newAge < 65 && !Objects.equals(newAge, employee.getAge())) {
            employee.setAge(newAge);
            LOGGER.debug("Updating employee-" + id + ", age updated");
        }

        if (newEmail != null && newEmail.length() > 0 && !Objects.equals(newEmail, employee.getEmail())) {
            Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmail(newEmail);

            if (employeeByEmail.isPresent()) {
                LOGGER.debug("Terminating updation of employee-" + id + ", email already exists");
                throw new IllegalArgumentException("Email already taken, choose a different one");
            }

            employee.setEmail(newEmail);
            LOGGER.debug("Updating employee-" + id + ", email updated");
        }
    }

    public void deleteEmployee(Long id) {
        boolean doesEmployeeExist = employeeRepository.existsById(id);

        if (doesEmployeeExist)
            employeeRepository.deleteById(id);
        else {
            LOGGER.debug("Could not delete employee-" + id + ", Reason - Does not exist");
            throw new EntityNotFoundException("Employee (id - " + id + ") does not exist!!");
        }

        LOGGER.debug("Successfully deleted employee-" + id);
    }

    //Update employee - add department deptId to employee empId
    @Transactional
    public void assignDepartment(Long empId, Long deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + deptId + ") does not exist"));

        LOGGER.debug("Department exists, checking employee");

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException("Employee (id - " + empId + ") does not exist"));

        LOGGER.debug("Employee exists, assigning department");

        employee.setDepartment(department);
        LOGGER.debug("Employee-" + empId + " assigned to department-" + deptId);
    }

    //Save multiple employee records into DB asynchronously
    @Async
    public void saveFile(List<Employee> employees) {
        LOGGER.debug("Current thread :-- " + Thread.currentThread().getName() + ", allotted {} records", employees.size());
        employeeRepository.saveAll(employees);
    }

    // Helper method to convert CSV file to list of Employee objects
    public List<Employee> csvToEmployees(MultipartFile file) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("name", "email", "designation", "age", "departmentId")
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        LOGGER.debug("CSV Parser format set");

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );
             CSVParser csvParser = new CSVParser(bufferedReader, format)
        ) {
            List<Employee> employees = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            LOGGER.debug("CSVRecords fetched");

            for (CSVRecord csvRecord : csvRecords) {
                Optional<Department> department = departmentRepository.findById(
                        Long.parseLong(csvRecord.get("DepartmentId"))
                );

                if (!department.isPresent())
                    throw new EntityNotFoundException("Department not found");

                String email = csvRecord.get("Email");

                Employee employee = new Employee(
                        csvRecord.get("Name"),
                        email,
                        csvRecord.get("Designation"),
                        Integer.parseInt(csvRecord.get("Age")),
                        department.get()
                );

                if(employeeRepository.findEmployeeByEmail(email).isPresent())
                    throw new IllegalArgumentException("Email already exists - " + email);

                employees.add(employee);
            }

            LOGGER.debug("List created, saving to database");
            return employees;
        } catch (IOException e) {
            LOGGER.error("Couldn't parse CSV file : won't be proceeding further");
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
