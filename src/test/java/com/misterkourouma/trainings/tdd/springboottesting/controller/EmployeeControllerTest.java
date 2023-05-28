package com.misterkourouma.trainings.tdd.springboottesting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.SpringbootWebfluxApplication;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.controller.EmployeeController;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.dto.EmployeeDto;
import com.misterkourouma.trainings.tdd.springboottesting.springbootwebflux.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringbootWebfluxApplication.class)
@WebFluxTest(controllers = EmployeeController.class)
 class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webFluxTest;

    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() throws JsonProcessingException {

        //given - precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("abdoulaye")
                .lastName("kourouma")
                .email("admin@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(EmployeeDto.class))).willReturn(Mono.just(employeeDto));

        // when - action or behaviour that we are going to test
        WebTestClient.ResponseSpec response = webFluxTest.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

      @Test
        void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject(){

              //given - precondition or setup
              String employeeId = "123";
          EmployeeDto employeeDto = EmployeeDto.builder()
                  .firstName("abdoulaye")
                  .lastName("kourouma")
                  .email("admin@gmail.com")
                  .build();
          given(employeeService.getEmployee(employeeId)).willReturn(Mono.just(employeeDto));

              // when - action or behaviour that we are going to test
          WebTestClient.ResponseSpec response = webFluxTest.get().uri("/api/employees/{id}", employeeId).accept(MediaType.APPLICATION_JSON).exchange();

          // then - verify the output
          response.expectStatus().isOk()
                  .expectBody()
                  .consumeWith(System.out::println)
                  .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                  .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                  .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
        }

          @Test
            void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList(){

                  //given - precondition or setup
              EmployeeDto employeeDto = EmployeeDto.builder()
                      .firstName("abdoulaye")
                      .lastName("kourouma")
                      .email("admin@gmail.com")
                      .build();
              EmployeeDto employeeDto2 = EmployeeDto.builder()
                      .firstName("mariame")
                      .lastName("souare")
                      .email("mame@gmail.com")
                      .build();
              List<EmployeeDto> employeeDtoList = List.of(employeeDto, employeeDto2);
              Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(employeeDtoList);

              given(employeeService.getAllEmployees()).willReturn(employeeDtoFlux);
                  // when - action or behaviour that we are going to test
              WebTestClient.ResponseSpec response = webFluxTest.get().uri("/api/employees")
                      .accept(MediaType.APPLICATION_JSON)
                      .exchange();
              // then - verify the output
              response.expectStatus().isOk()
                      .expectBodyList(EmployeeDto.class)
                      .hasSize(employeeDtoList.size())
                      .consumeWith(System.out::println);
            }

              @Test
                void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){

                      //given - precondition or setup
                  String employeeId = "123";
                  EmployeeDto employeeDto = EmployeeDto.builder()
                          .firstName("abdoulaye")
                          .lastName("kourouma")
                          .email("admin@gmail.com")
                          .build();
                  given(employeeService.updateEmployee(any(EmployeeDto.class), anyString())).willReturn(Mono.just(employeeDto));

                  // when - action or behaviour that we are going to test
                  WebTestClient.ResponseSpec response = webFluxTest.put().uri("/api/employees/{id}", employeeId)
                          .contentType(MediaType.APPLICATION_JSON)
                          .accept(MediaType.APPLICATION_JSON)
                          .body(Mono.just(employeeDto), EmployeeDto.class)
                          .exchange();

                  // then - verify the output
                  response.expectStatus().isOk()
                          .expectBody()
                          .consumeWith(System.out::println)
                          .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                          .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                          .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
                }

                  @Test
                    void givenEmployeeId_whenDeleteEmployee_thenReturnVoid(){

                          //given - precondition or setup
                          String employeeId = "123";
                          given(employeeService.deleteEmployee(anyString())).willReturn(Mono.empty());

                          // when - action or behaviour that we are going to test
                      WebTestClient.ResponseSpec response = webFluxTest.delete().uri("/api/employees/{id}", employeeId).exchange();

                      // then - verify the output
                      response.expectStatus().isNoContent()
                              .expectBody()
                              .consumeWith(System.out::println);
                      verify(employeeService, times(1)).deleteEmployee(anyString());
                    }
}
