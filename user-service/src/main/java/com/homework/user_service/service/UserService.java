package com.homework.user_service.service;

import com.homework.user_service.enums.OperationType;
import com.homework.user_service.dto.UserEventDto;
import lombok.RequiredArgsConstructor;
import com.homework.user_service.dto.UserRequestDto;
import com.homework.user_service.dto.UserResponseDto;
import com.homework.user_service.entity.User;
import com.homework.user_service.exception.EmailAlreadyExistsException;
import com.homework.user_service.exception.UserNotFoundException;
import com.homework.user_service.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;

    private UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("This email already exists!");
        }

        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setAge(userRequestDto.getAge());
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setOperationType(OperationType.CREATE);
        userEventDto.setEmail(user.getEmail());
        kafkaTemplate.send("user-created", userEventDto);
        return toResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        if (!user.getEmail().equals(userRequestDto.getEmail())) {
            if (userRepository.existsByEmail(userRequestDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists!");
            }
        }

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setAge(userRequestDto.getAge());

        user = userRepository.save(user);
        return toResponseDto(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found!");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));
        UserEventDto userEventDto = new UserEventDto();
        userEventDto.setOperationType(OperationType.DELETE);
        userEventDto.setEmail(user.getEmail());
        kafkaTemplate.send("user-deleted", userEventDto);

        userRepository.deleteById(id);
    }
}