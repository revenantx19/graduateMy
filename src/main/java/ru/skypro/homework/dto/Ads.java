package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private Integer count;

    @Schema(description = "автор объявлений")
    private Ad ad;

}
