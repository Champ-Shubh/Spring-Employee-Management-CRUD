package com.example.devtraining.service;

import com.example.devtraining.model.Department;
import com.example.devtraining.model.Employee;
import com.example.devtraining.repository.DepartmentRedisRepository;
import com.example.devtraining.repository.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentRedisRepository redisRepository;
    private final Logger logger = LogManager.getLogger(DepartmentService.class);

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentRedisRepository redisRepository) {
        this.departmentRepository = departmentRepository;
        this.redisRepository = redisRepository;
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id){
        Department cachedDepartment = null;
        try{
            cachedDepartment = redisRepository.getDepartmentById(id);
        }
        catch (Exception e){
            logger.warn("No department yet cached yet with id - " + id);
        }

        if(Objects.nonNull(cachedDepartment))
            return cachedDepartment;

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + id + ") does not exist"));

        logger.debug("Department-" + id + " exists, fetching data");

        //cache this new entry because it doesn't already exist
        logger.debug("Saving to cache because it was absent");
        redisRepository.addDepartment(department);

        return department;
    }

    public void addDepartment(Department department) {
        departmentRepository.save(department);

        //Add new entry to cache too
        logger.debug("Saving to cache too");
        redisRepository.addDepartment(department);
    }

    public Set<Employee> getEmployeesInDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + id + ") does not exist"));

        logger.debug("Department-" + id + " exists, fetching employees");
        return department.getEmployees();
    }

    @Transactional
    public void deleteDepartment(Long id) {
        boolean doesDepartmentExist = departmentRepository.existsById(id);

        if (!doesDepartmentExist) {
            logger.debug("Terminating deletion : department-" + id + " does not exist");
            throw new EntityNotFoundException("Department (id - " + id + ") does not exist");
        }

        //Remove this entry from cache too
        logger.debug("Removing from cache");
        redisRepository.deleteDepartment(id);

        departmentRepository.deleteById(id);
    }

    @Transactional
    public void updateDepartment(Long id, Department newDepartment) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department (id - " + id + ") does not exist"));

        logger.debug("Department exists, proceeding with updation");

        String deptName = newDepartment.getDepartmentName();
        String deptCode = newDepartment.getDepartmentCode();

        if (deptName != null && deptName.length() > 0 && !Objects.equals(deptName, department.getDepartmentName())) {
            department.setDepartmentName(deptName);
            logger.debug("Updating department-" + id + ", departmentName updated");
        }

        if (deptCode != null && deptCode.length() > 0 && !Objects.equals(deptCode, department.getDepartmentCode())) {
            department.setDepartmentCode(deptCode);
            logger.debug("Updating department-" + id + ", departmentCode updated");
        }

        //Update this entry in cache too
        logger.debug("Updating entry in cache");
        redisRepository.updateDepartment(id, department);
    }
}
