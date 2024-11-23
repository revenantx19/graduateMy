package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    /**
     * Метод обновления пароля авторизированного пользователя
     *
     * @param newPassword    объект содержащий старый и новый пароли
     * @param authentication объект содержащий данные авторизированного пользователя
     * @return httpstatus
     */
    @Operation(summary = "Обновление пароля")
    @PostMapping(path = "/users/set_password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
    })
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword,
                                                  Authentication authentication) {
        log.info("Вошли в метод setPassword, класса UserController.\n" +
                "Принят объект newPassword: " + newPassword.toString());
        if (newPassword != null) {
            userService.setPassword(newPassword, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Метод, через который фронт получает актуальную информацию о пользователе
     *
     * @param authentication объект содержащий данные авторизированного пользователя
     * @return объект User с информацией об авторизованном пользователе
     */

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @GetMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<User> getUser(Authentication authentication) {
        log.info("Метод getUser, класса UserController.");
        User userDto = userService.getUser(authentication.getName());
        if (userDto != null) {
            log.info("Получен объект User {}", userDto);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Метод обновления данных пользователя
     *
     * @param updateUser     объект с обновленными данными
     * @param authentication объект содержащий данные авторизированного пользователя
     * @return httpstatus
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @PatchMapping(path = "/users/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUser.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser,
                                                 Authentication authentication) {
        log.info("Вошли в метод updateUser контроллера UserController. Принят объект updateUser: " + updateUser);
        if (authentication != null) {
            userService.updateUser(updateUser, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Метод обновления аватара авторизированного пользователя
     *
     * @param image          принимается изображение
     * @param authentication объект содержащий данные авторизированного пользователя
     * @return httpstatus
     * @throws IOException
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @PatchMapping(path = "/users/me/image", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateUserImage(@RequestPart(value = "image", required = true) MultipartFile image,
                                             Authentication authentication) throws IOException {
        log.info("Вошли в метод uploadUserImage, класса UserController. Принят файл image: " + image.toString());
        if (authentication != null) {
            userService.updateUserAvatar(image, authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    /**
     * Get метод получения аватарки пользователя по отправляемым из
     * фронтенда имени файла, который был передан из getUser метода {@link UserController}
     *
     * @param fileName имя файла изображения
     * @return массив байтов изображения byte[]
     * @throws IOException
     * Для полного понимания: первоначально фронтенд вызывает метод getUser контроллера UserController
     *                     и принимает из него dto User, который содержит поле String image, которое хранит
     *                     путь+имя файла аватарки пользователя. Далее фронт конкатенирует имя файла с http://localhost:8080
     *                     и обращается по этому пути, из-за этого создан данный get метод, который начинается с /avatar/,
     *                     так как в этой папке (avatar) сохраняются все загруженные изображения. Причем фронту не важно
     *                     какой путь будет передан, вернёт он только имя файла, НО фронт валится, если ему передать
     *                     обратный слэш "\", так как при конкатенации адрес получается битым и не происходит обращения
     *                     по нашему get методу. В общем убито около 3 дней на эту прекрасную историю...
     */
    @GetMapping(value = "/avatar/{fileName}",
            produces = {MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getAvatarImageByFilename(@PathVariable String fileName) throws IOException {
        log.info("Вошли в метод getAvatarImageByFilename, класса UserController.");
        byte[] avatarData = userService.findAvatarImageByFilename(fileName);
        log.info("Получен массив байт (выведем первый байт): {}", avatarData[0]);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_GIF)
                .body(avatarData);
    }

}
