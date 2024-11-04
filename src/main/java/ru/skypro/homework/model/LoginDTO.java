package ru.skypro.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class LoginDTO {

    @Schema(minLength = 8, maxLength = 16, description = "логин")
    @Id
    private String username;

    @Schema(minLength = 8, maxLength = 16, description = "пароль")
    private String password;
}
