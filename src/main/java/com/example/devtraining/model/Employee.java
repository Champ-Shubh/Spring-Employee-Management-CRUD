package com.example.devtraining.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;

    private String designation;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee() {
    }

    public Employee(Long id, String name, String email, String designation, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.age = age;
    }

    public Employee(String name, String email, String designation, Integer age, Department department) {
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.age = age;
        this.department = department;
    }

    public Employee(String name, String email, String designation, Integer age) {
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        Department oldDepartment = this.department;

        if (Objects.equals(department, oldDepartment))
            return;

        //Assign department
        this.department = department;

        //Remove this employee from old department's employee list
        if (oldDepartment != null)
            oldDepartment.removeEmployee(this);

        //Add this employee to new department's employee list
        if (department != null)
            department.assignEmployee(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", designation='" + designation + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(designation, employee.designation) &&
                Objects.equals(age, employee.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, designation, age);
    }
}
