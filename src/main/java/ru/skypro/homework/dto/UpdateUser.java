package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUser {


    @Schema(minLength = 3, maxLength = 10, description = "имя пользователя")
    private String firstName;

    @Schema(minLength = 3, maxLength = 10, description = "фамилия пользователя")
    private String lastName;

    @Schema(pattern = "pattern: \\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}\n",
            description = "телефон пользователя")
    private String phone;

}
