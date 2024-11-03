package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateOrUpdateAd {

    @Schema(minLength = 4, maxLength = 32, description = "заголовок объявления")
    private String title;

    @Schema(minimum = "0", maximum = "10000000", description = "цена объявления")
    private Integer price;

    @Schema(minLength = 8, maxLength = 64, description = "описание объявления")
    private String description;
}
