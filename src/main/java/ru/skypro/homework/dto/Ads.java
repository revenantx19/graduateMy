package ru.skypro.homework.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private Integer count;
    private Ad[] results;

    public Ads(Integer count, Ad[] results) {
        this.count = count;
        this.results = results;
    }
}
