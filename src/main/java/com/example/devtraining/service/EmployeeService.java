package com.example.devtraining.service;

import com.example.devtraining.model.Department;
import com.example.devtraining.model.Employee;
import com.example.devtraining.repository.DepartmentRepository;
import com.example.devtraining.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    private final Logger logger = LogManager.getLogger(EmployeeService.class);

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
            logger.error("Email already exists - Terminating employee creation");
            throw new IllegalArgumentException("Email already taken, choose a different one");
        }

        employeeRepository.save(employee);
    }

    //Add a new employee with department set to department with id deptId
    public void addEmployeeWithDepartment(Employee employee, Long deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + deptId + ") does not exist"));

        logger.debug("Department exists, proceeding with assignment");

        employee.setDepartment(department);
        logger.debug("Department assigned to employee");

        addEmployee(employee);
    }

    //Perform validation and update employee
    @Transactional
    public void updateEmployee(Long id, Employee newEmployee) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee (id - " + id + ") does not exist"));

        logger.debug("Employee does exist, proceeding with updation");

        String newName = newEmployee.getName();
        String newDesignation = newEmployee.getDesignation();
        String newEmail = newEmployee.getEmail();
        Integer newAge = newEmployee.getAge();

        if (newName != null && newName.length() > 0 && !Objects.equals(newName, employee.getName())) {
            employee.setName(newName);
            logger.debug("Updating employee-" + id + ", name updated");
        }

        if (newDesignation != null && newDesignation.length() > 0 && !Objects.equals(newDesignation, employee.getDesignation())) {
            employee.setDesignation(newDesignation);
            logger.debug("Updating employee-" + id + ", designation updated");
        }

        if (newAge != null && newAge >= 0 && newAge < 65 && !Objects.equals(newAge, employee.getAge())) {
            employee.setAge(newAge);
            logger.debug("Updating employee-" + id + ", age updated");
        }

        if (newEmail != null && newEmail.length() > 0 && !Objects.equals(newEmail, employee.getEmail())) {
            Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmail(newEmail);

            if (employeeByEmail.isPresent()) {
                logger.debug("Terminating updation of employee-" + id + ", email already exists");
                throw new IllegalArgumentException("Email already taken, choose a different one");
            }

            employee.setEmail(newEmail);
            logger.debug("Updating employee-" + id + ", email updated");
        }
    }

    public void deleteEmployee(Long id) {
        boolean doesEmployeeExist = employeeRepository.existsById(id);

        if (doesEmployeeExist)
            employeeRepository.deleteById(id);
        else {
            logger.debug("Could not delete employee-" + id + ", Reason - Does not exist");
            throw new EntityNotFoundException("Employee (id - " + id + ") does not exist!!");
        }

        logger.debug("Successfully deleted employee-" + id);
    }

    //Update employee - add department deptId to employee empId
    @Transactional
    public void assignDepartment(Long empId, Long deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + deptId + ") does not exist"));

        logger.debug("Department exists, checking employee");

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException("Employee (id - " + empId + ") does not exist"));

        logger.debug("Employee exists, assigning department");

        employee.setDepartment(department);
        logger.debug("Employee-" + empId + " assigned to department-" + deptId);
    }
}
