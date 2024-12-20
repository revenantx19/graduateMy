package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Login {

    @Schema(minLength = 8, maxLength = 16, description = "логин")
    private String username;

    @Schema(minLength = 8, maxLength = 16, description = "пароль")
    private String password;
}
