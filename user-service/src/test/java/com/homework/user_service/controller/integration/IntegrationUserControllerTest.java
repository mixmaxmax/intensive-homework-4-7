package com.homework.user_service.controller.integration;

import com.homework.user_service.dto.UserRequestDto;
import com.homework.user_service.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationUserControllerTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();
    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api/users";
    }
    
    @Test
    void getUserById_NotFound() {
        try {
            restTemplate.getForEntity(url + "/-999", String.class);
            fail("Expect error. Id not found");
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }
    
    @Test
    void createUser_Error_EmailAlreadyExists() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Иванов Иван");
        userRequestDto.setEmail("i.ivanov@mail.com");
        userRequestDto.setAge(25);
        restTemplate.postForEntity(url, userRequestDto, UserResponseDto.class);
        
        try {
            restTemplate.postForEntity(url, userRequestDto, UserResponseDto.class);
            fail("Expect error. Email already exists");
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }
    
    @Test
    void updateUser_NotFound() {
        UserRequestDto userRequestDto = new UserRequestDto();

        try {
            restTemplate.put(url + "/-999", userRequestDto);
            fail("Expect error. User not found");
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }
    
    @Test
    void deleteUser_NotFound() {
        try {
            restTemplate.delete(url + "/-999");
            fail("Expect error. User not found");
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }
}