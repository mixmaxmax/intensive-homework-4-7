package com.homework.user_service.controller.unit;

import com.homework.user_service.dto.UserRequestDto;
import com.homework.user_service.dto.UserResponseDto;
import com.homework.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class UnitUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllUsers_Success() throws Exception {
        UserResponseDto dto1 = new UserResponseDto();
        LocalDateTime time = LocalDateTime.now();

        dto1.setId(1);
        dto1.setName("Иванов Иван");
        dto1.setEmail("i.ivan@mail.com");
        dto1.setAge(25);
        dto1.setCreatedAt(time);

        UserResponseDto dto2 = new UserResponseDto();
        dto2.setId(2);
        dto2.setName("Денисов Денис");
        dto2.setEmail("d.denisov@mail.com");
        dto2.setAge(30);
        dto2.setCreatedAt(time);

        List<UserResponseDto> users = List.of(dto1, dto2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Иванов Иван"))
                .andExpect(jsonPath("$[0].email").value("i.ivan@mail.com"))
                .andExpect(jsonPath("$[0].age").value(25))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Денисов Денис"))
                .andExpect(jsonPath("$[1].email").value("d.denisov@mail.com"))
                .andExpect(jsonPath("$[1].age").value(30));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_Success() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        UserResponseDto dto = new UserResponseDto();
        dto.setId(1);
        dto.setName("Иванов Иван");
        dto.setEmail("i.ivan@mail.com");
        dto.setAge(25);
        dto.setCreatedAt(time);

        when(userService.getUserById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иванов Иван"))
                .andExpect(jsonPath("$.email").value("i.ivan@mail.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService).getUserById(1);
    }

    @Test
    void createUser_Success() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1);
        userResponseDto.setName("Иванов Иван");
        userResponseDto.setEmail("i.ivan@mail.com");
        userResponseDto.setAge(25);

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        String json = "{\"name\":\"Иванов Иван\",\"email\":\"i.ivan@mail.com\",\"age\":25}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иванов Иван"))
                .andExpect(jsonPath("$.email").value("i.ivan@mail.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void updateUser_Success() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1);
        userResponseDto.setName("Иванов Иван");
        userResponseDto.setEmail("i.ivan@mail.com");
        userResponseDto.setAge(25);

        when(userService.updateUser(eq(1), any(UserRequestDto.class))).thenReturn(userResponseDto);

        String json = "{\"name\":\"Иванов Иван\",\"email\":\"i.ivan@mail.com\",\"age\":25}";

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иванов Иван"))
                .andExpect(jsonPath("$.email").value("i.ivan@mail.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService).updateUser(eq(1), any(UserRequestDto.class));
    }

    @Test
    void deleteUser_Success() throws Exception {
        Integer userId = 1;
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }
}