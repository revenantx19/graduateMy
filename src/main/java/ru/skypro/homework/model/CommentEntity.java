package ru.skypro.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId; // ид комментария
    private Integer author; // ид автора объявления
    private String authorImage; // ссылка на аватар автора
    private String authorFirstName; // имя создателя комментария
    private Integer createAd; //дата и время создания комментарий
    private String text; //текст комментарий

    //2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    //3
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private AdEntity ad;

}
