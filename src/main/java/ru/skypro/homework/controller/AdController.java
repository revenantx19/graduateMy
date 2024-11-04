package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.ExtendedAdDTO;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Объявления")
public class AdController {

    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @GetMapping(path = "/ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            }),
    })
    public ResponseEntity<?> getAllAds() {
        log.info("Вы вошли в метод getAds");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @PostMapping(path = "/ads", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                   @RequestPart(value = "image", required = true) MultipartFile image) {
        log.info("Вы вошли в метод addAds");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExtendedAdDTO.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAds(@PathVariable int id) {
        log.info("Вы вошли в метод getAdsById");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @DeleteMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> removeAd(@PathVariable("id") int id) {
        log.info("Вы вошли в метод deleteAdsById");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление информации об объявлении", tags = {"Объявления"})
    @PatchMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Вы вошли в метод updateAdsById");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @GetMapping(path = "/ads/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAdsMe() {
        log.info("Вы вошли в метод getAdsCurrentUser");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @PatchMapping(path = "/ads/{id}/image", consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "OK",
                         content = @Content(mediaType = "application/octet-stream",
                                            array = @ArraySchema(schema = @Schema(type = "string", format = "byte")))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> updateImage(@PathVariable("id") int id,
                                         @RequestPart(value = "image", required = true) MultipartFile image) {
        log.info("Вы вошли в метод updateAdsImage");
        return ResponseEntity.ok().build();
    }
}
