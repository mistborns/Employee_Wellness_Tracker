package com.example.employeewellnesstracker.controller;

import com.example.employeewellnesstracker.model.Employee;
import com.example.employeewellnesstracker.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    EmployeeService employeeService;

    @Autowired
    public EmployeeController( EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    //fetches all employees
    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeByID(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id , @RequestBody Employee employee){
        return employeeService.updateEmployee(id, employee);
    }

    @PutMapping("/{id}/promote")
    public ResponseEntity<Employee> promoteEmployee(@PathVariable Long id) {
        Employee promotedEmployee = employeeService.promoteEmployee(id);
        if (promotedEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(promotedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build(); // Return 204 No Content on successful delete
    }

    @GetMapping("/departments/{department}")
    public List<Employee> getEmployeesByDepartments(@PathVariable String department){
        return employeeService.getEmployeesByDepartment(department);
    }


}
