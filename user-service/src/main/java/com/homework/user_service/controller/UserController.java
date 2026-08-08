package com.homework.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.homework.user_service.dto.UserRequestDto;
import com.homework.user_service.dto.UserResponseDto;
import com.homework.user_service.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "UserController methods")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final String allUsersRef = "all-users";
    private final String updateRef = "update";
    private final String deleteRef = "delete";

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Успешный ответ",
        content = @Content(
        mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
    public CollectionModel<EntityModel<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        List<EntityModel<UserResponseDto>> entityModels = new ArrayList<>();

        for (UserResponseDto user: users) {
            EntityModel<UserResponseDto> entityModel = EntityModel.of(user);
            entityModel.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
            entityModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel(allUsersRef));
            entityModel.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel(updateRef));
            entityModel.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel(deleteRef));
            entityModels.add(entityModel);
        }

        CollectionModel<EntityModel<UserResponseDto>> collectionModel = CollectionModel.of(entityModels);
        collectionModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return collectionModel;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(
                responseCode = "200", description = "Пользователь найден",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(
                responseCode = "500", description = "Внутренняя ошибка сервера",
                content = @Content)
    })
    public EntityModel<UserResponseDto> getUserById(@Parameter(description = "ID пользователя") @PathVariable Integer id) {
        UserResponseDto user = userService.getUserById(id);
        EntityModel<UserResponseDto> entityModel = EntityModel.of(user);

        entityModel.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        entityModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel(allUsersRef));
        entityModel.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel(updateRef));
        entityModel.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel(deleteRef));

        return entityModel;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить нового пользователя")
    @ApiResponses({
            @ApiResponse(
                responseCode = "201", description = "Пользователь создан",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(
                responseCode = "400", description = "Ошибка валидации",
                content = @Content),
            @ApiResponse(
                responseCode = "500", description = "Внутренняя ошибка сервера",
                content = @Content)
    })
    public EntityModel<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto user = userService.createUser(request);
        EntityModel<UserResponseDto> entityModel = EntityModel.of(user);

        entityModel.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel(allUsersRef));
        entityModel.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), request)).withRel(updateRef));
        entityModel.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel(deleteRef));

        return entityModel;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные пользователя")
    @ApiResponses({
            @ApiResponse(
                responseCode = "200", description = "Данные обновлены",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(
                responseCode = "400", description = "Ошибка валидации",
                content = @Content),
            @ApiResponse(
                responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public EntityModel<UserResponseDto> updateUser(
            @Parameter(description = "ID пользователя")
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestDto request) {

        UserResponseDto user = userService.updateUser(id, request);
        EntityModel<UserResponseDto> entityModel = EntityModel.of(user);

        entityModel.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        entityModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel(allUsersRef));
        entityModel.add(linkTo(methodOn(UserController.class).updateUser(id, request)).withRel(updateRef));
        entityModel.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel(deleteRef));


        return entityModel;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(
                responseCode = "204", description = "Пользователь удален",
                content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                content = @Content)
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID пользователя") @PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
