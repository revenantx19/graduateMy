package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.model.CommentEntity;

@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private Integer count;

    private CommentEntity result;

}
