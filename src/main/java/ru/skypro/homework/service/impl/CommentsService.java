package ru.skypro.homework.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.repository.CommentRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public Comment addComment(int id,
                              CreateOrUpdateComment createOrUpdateComment,
                              String username) {
        CommentEntity commentEntity = commentMapper.toCommentEntity(createOrUpdateComment,
                                                                    username,
                                                                    id);
        commentRepository.save(commentEntity);
        log.info("Комментарий с текстом: \"" + commentEntity.getText() + "\" успешно сохранён к объявлению с id: " + id);
        Comment comment = commentMapper.toCommentDto(commentEntity);
        return comment;
    }

    public Comments getAllCommentsAd(int id, String name) {
        log.info("Вошли в метод getAllCommentsAd сервиса CommentService");
        boolean existComments = commentRepository.existsCommentAdByAdId(id);
        if (existComments) {
            List<Comment> comments = commentRepository.findAllCommentsByAdId(id).stream()
                    .map(commentEntity -> commentMapper.toCommentDto(commentEntity))
                    .collect(Collectors.toList());
            log.info("Отправлен массив комментариев " + comments);
            return new Comments(comments.size(), comments.toArray(new Comment[0]));
        } else {
            log.info("Получен пустой массив комментариев ");
            return new Comments(0, new Comment[0]);
        }

    }

    public void deleteComment(int adId, int commentId) {
        log.info("Вошли в метод deleteComment сервиса CommentServiceImpl");
        commentRepository.deleteCommentByCommentIdAndAdId(adId, commentId);
    }


    public Comment updateComment(int adId, int commentId, String text) {
        log.info("Вошли в метод updateComment сервиса CommentService");
        commentRepository.updateCommentTextByCommentIdAndAdId(adId, commentId, text);
        return commentMapper.toCommentDto(commentRepository.findCommentByCommentIdAndAdId(adId, commentId));

    }
}
