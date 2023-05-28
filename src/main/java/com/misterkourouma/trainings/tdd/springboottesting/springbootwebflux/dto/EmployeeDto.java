package com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private String id;
    private String firstName;
    private String  lastName;
    private String email;
}
