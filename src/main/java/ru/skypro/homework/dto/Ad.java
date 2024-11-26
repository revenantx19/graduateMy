package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Ad {

    @Schema(description = "id автора объявления")
    private Integer author;
    @Schema(description = "ссылка на картинку объявления")
    private String image;
    @Schema(description = "id объявления")
    private Integer pk;
    @Schema(description = "цена объявления")
    private Integer price;
    @Schema(description = "заголовок объявления")
    private String title;

    public Ad(Integer author, String image, Integer id, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = id;
        this.price = price;
        this.title = title;
    }

    public Ad() {

    }
}
