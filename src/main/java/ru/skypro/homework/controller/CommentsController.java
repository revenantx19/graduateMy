package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Комментарии")
public class CommentsController {

    @Operation(summary = "Получение комментариев объявления", tags = {"Комментарии"})
    @GetMapping(path = "/ads/{id}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comments.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getComments(@PathVariable("id") int id) {
        log.info("Вы вошли в метод getCommentsAds");
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Добавление комментария к объявлению", tags = {"Комментарии"})
    @PostMapping(path = "/ads/{id}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> addComment(@PathVariable("id") int id) {
        log.info("Вы вошли в метод addCommentsAds");
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Удаление комментария", tags = {"Комментарии"})
    @DeleteMapping(path = "/ads/{adId}/comments/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> deleteComment(@PathVariable("adId") int adId,
                                           @PathVariable("commentId") int commentId) {
        log.info("Вы вошли в метод deleteCommentsAds");
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Обновление комментария", tags = {"Комментарии"})
    @PatchMapping(path = "/ads/{adId}/comments/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)),
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateComment(@PathVariable("adId") int adId,
                                           @PathVariable("commentId") int commentId,
                                           @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        log.info("Вы вошли в метод updateCommentsAds");
        return ResponseEntity.ok().build();
    }
}
