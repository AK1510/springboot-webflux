package com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.repository;

import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
