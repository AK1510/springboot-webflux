package com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.mapper;

import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.dto.EmployeeDto;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee){
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto){
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
    }
}
