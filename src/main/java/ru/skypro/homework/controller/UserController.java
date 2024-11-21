package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @Operation(summary = "Обновление пароля")
    @PostMapping(path = "/users/set_password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
    })
    public ResponseEntity<NewPassword> setPassword(@RequestBody NewPassword newPassword,
                                                   Authentication authentication) {
        log.info("Вошли в метод setPassword, класса UserController. Принят объект newPassword: " + newPassword.toString());
        if (newPassword != null) {
            userService.setPassword(newPassword, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getUser() {
        log.info("Метод getUser, класса UserController.");
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUser.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser,
                                        Authentication authentication) {
        log.info("Вошли в метод updateUser контроллера UserController. Принят объект updateUser: " + updateUser);
        userService.updateUser(updateUser, authentication.getName());
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping(path = "/users/me/image", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateUserImage(@RequestPart(value = "image", required = true) MultipartFile image,
                                             Authentication authentication) throws IOException {
        log.info("Вошли в метод uploadUserImage, класса UserController. Принят файл image: " + image.toString());
        userService.updateUserImage(image, authentication.getName());
        return ResponseEntity.ok().build();

    }

    @GetMapping(value = "/users/images/{id}",
            produces = {MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getAvatarImageByUserId(@PathVariable int id,
                                                         Authentication authentication) throws IOException {
        log.info("Вошли в метод getAvatarImageByUsername, класса UserController.");
        byte[] avatarData = userService.findAvatarImageByUserId(id, authentication.getName());
        log.info("Вошли в метод getAvatarImageByUsername, класса UserController.+" +
                "\nПолучен массив байт (ведем первый байт) {}", avatarData[0]);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // Можно выбрать необходимый тип
                .body(avatarData);
    }

}
