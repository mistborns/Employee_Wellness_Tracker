package com.example.employeewellnesstracker.controller;

import com.example.employeewellnesstracker.model.Employee;
import com.example.employeewellnesstracker.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id , @RequestBody Employee employee){
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeByID(id);
    }

    @DeleteMapping("/{id}")
    public void DeletedEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/departments/{department}")
    public List<Employee> getEmployeesByDepartments(@PathVariable String department){
        return employeeService.getEmployeesByDepartment(department);
    }


}
