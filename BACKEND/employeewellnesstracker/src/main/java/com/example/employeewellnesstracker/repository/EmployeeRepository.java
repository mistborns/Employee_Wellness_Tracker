package com.example.employeewellnesstracker.repository;

import com.example.employeewellnesstracker.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartment(String department);
}
