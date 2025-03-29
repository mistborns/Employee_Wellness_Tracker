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
        return employeeRepository.findById(id).map(employee -> {
                    if (updatedEmployee.getName() != null) {
                        employee.setName(updatedEmployee.getName());
                    }
                    if (updatedEmployee.getEmail() != null) {
                        employee.setEmail(updatedEmployee.getEmail());
                    }
                    if (updatedEmployee.getDepartment() != null) {
                        employee.setDepartment(updatedEmployee.getDepartment());
                    }
                    if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().isEmpty()) {
                        employee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
                    }
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }


    public Employee promoteEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null || employee.getIsAdmin()) {
            return null;//check if exists or is an admin
        }
        employee.setIsAdmin(true);
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

    public boolean deleteEmployee(Long id){
        if(!employeeRepository.existsById(id)){
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }

    //checks hashed and raw password
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
