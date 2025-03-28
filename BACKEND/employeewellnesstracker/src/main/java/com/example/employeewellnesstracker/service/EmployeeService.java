package com.example.employeewellnesstracker.service;

import com.example.employeewellnesstracker.model.Employee;
import com.example.employeewellnesstracker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee){
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }


    public Employee updateEmployee(Long id, Employee updatedEmployee){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setDepartment(updatedEmployee.getDepartment());
        return employeeRepository.save(employee);
    }


    public Employee getEmployeeByID(Long id){
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public Optional<Employee> getEmployeeByEmail(String email){
        return employeeRepository.findByEmail(email);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByDepartment(String department){
        return employeeRepository.findByDepartment(department);
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }

    //checks hashed and raw password
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
