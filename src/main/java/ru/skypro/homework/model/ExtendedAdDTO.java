package ru.skypro.homework.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ExtendedAdDTO {

    @Id
    @Schema(description = "id объявления")
    private Integer pk;

    @Schema(description = "имя авто объявления")
    private String authorFirstName;

    @Schema(description = "фамилия автора объявления")
    private String authorLastName;

    @Schema(description = "описание объявления")
    private String description;

    @Schema(description = "логин автора объявления")
    private String email;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "телефон автора объявления")
    private String phone;

    @Schema(description = "цена объявления")
    private Integer price;

    @Schema(description = "заголовок объявления")
    private String title;

}
