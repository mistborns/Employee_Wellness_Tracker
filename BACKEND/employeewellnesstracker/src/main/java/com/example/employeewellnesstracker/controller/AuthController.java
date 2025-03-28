package com.example.employeewellnesstracker.controller;

import com.example.employeewellnesstracker.model.Employee;
import com.example.employeewellnesstracker.repository.EmployeeRepository;
import com.example.employeewellnesstracker.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Employee employee) {
        if (employeeService.getEmployeeByEmail(employee.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        Employee savedEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok(Map.of("message", "Registration successful", "employeeId", savedEmployee.getId()));
    }

    // Employee Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Employee loginEmployee) {
        Optional<Employee> optionalEmployee = employeeService.getEmployeeByEmail(loginEmployee.getEmail());
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get(); // get employee object from optional

            if (employeeService.checkPassword(loginEmployee.getPassword(), employee.getPassword())) {
                return ResponseEntity.ok(Map.of("employeeId", employee.getId(),
                        "name", employee.getName(), "isAdmin", employee.getIsAdmin()));
            }
        }
        // if email doesnt exist or password incorrect
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

}
