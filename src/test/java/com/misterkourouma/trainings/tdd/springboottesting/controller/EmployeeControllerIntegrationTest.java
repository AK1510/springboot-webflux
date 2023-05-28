package com.misterkourouma.trainings.tdd.springboottesting.controller;


import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.SpringbootWebfluxApplication;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.dto.EmployeeDto;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.repository.EmployeeRepository;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringbootWebfluxApplication.class})
 class EmployeeControllerIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp(){
        System.out.println("before");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployeeObject() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("john")
                .lastName("doe")
                .email("john@gmail.com")
                .build();

        // when - action or behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the output
        response.expectStatus()
                .isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());

    }

    @Test
    void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        String employeeId = "";
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("john")
                .lastName("doe")
                .email("john@gmail.com")
                .build();
        EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto).block();

        // when - action or behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/{id}", savedEmployeeDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(savedEmployeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(savedEmployeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(savedEmployeeDto.getEmail());
    }

    @Test
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("jane")
                .lastName("doe")
                .email("jane@gmail.com")
                .build();
        EmployeeDto employeeDto2 = EmployeeDto.builder()
                .firstName("steve")
                .lastName("doe")
                .email("steve@gmail.com")
                .build();
        List<EmployeeDto> employeeDtoList = List.of(employeeDto, employeeDto2);
        employeeDtoList.forEach( employeeDto1 ->{
            employeeService.saveEmployee(employeeDto1).block();
        });

        // when - action or behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees").accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println)
                .hasSize(employeeDtoList.size());
    }

      @Test
        void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){

              //given - precondition or setup
          EmployeeDto employeeDto = EmployeeDto.builder()
                  .firstName("jane")
                  .lastName("doe")
                  .email("jane@gmail.com")
                  .build();
          EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

              // when - action or behaviour that we are going to test
          WebTestClient.ResponseSpec response = webTestClient.delete().uri("/api/employees/{id}", savedEmployee.getId())
                  .accept(MediaType.APPLICATION_JSON)
                  .exchange();

          // then - verify the output
          response.expectStatus().isNoContent()
                  .expectBody()
                  .consumeWith(System.out::println);

        }

          @Test
            void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee(){

                  //given - precondition or setup
              EmployeeDto employeeDto = EmployeeDto.builder()
                      .firstName("jane")
                      .lastName("doe")
                      .email("jane@gmail.com")
                      .build();
              EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();
              EmployeeDto updatedEmployee = EmployeeDto.builder()
                      .firstName("abdoul")
                      .lastName("kroum")
                      .email("abdoul@gmail.com")
                      .build();

                  // when - action or behaviour that we are going to test
              WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", savedEmployee.getId())
                      .contentType(MediaType.APPLICATION_JSON)
                      .body(Mono.just(updatedEmployee), EmployeeDto.class)
                      .accept(MediaType.APPLICATION_JSON)
                      .exchange();

              // then - verify the output
              response.expectStatus().isOk()
                      .expectBody().consumeWith(System.out::println)
                      .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
                      .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
                      .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
            }
}
