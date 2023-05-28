package com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.service;

import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.dto.EmployeeDto;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.entity.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> getEmployee(String employeeId);
    Flux<EmployeeDto> getAllEmployees();

    Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId);

    Mono<Void> deleteEmployee(String employeeId);
}
