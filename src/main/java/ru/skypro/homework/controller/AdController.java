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
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.service.impl.AdServiceImpl;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Объявления")
public class AdController {

    private final AdServiceImpl adServiceImpl;

    public AdController(AdServiceImpl adServiceImpl) {
        this.adServiceImpl = adServiceImpl;
    }

    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @GetMapping(path = "/ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            }),
    })
    public ResponseEntity<?> getAllAds() {
        log.info("Метод getAllAds, класса AdController");
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
        log.info("Метод addAds, класса AdController. Приняты: \n" +
                "Новое объявление или обновление имеющегося " + properties.toString() +
                "\nИзображение объявления" + image.getOriginalFilename());
        adServiceImpl.addAd(properties);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdEntity.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> getAdsById(@PathVariable int id) {
        log.info("Метод getAdsById, класса AdController. Принят: \n" +
                "(int) id " + id);
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
    public ResponseEntity<?> removeAdById(@PathVariable("id") int id) {
        log.info("Метод deleteAdsById, класса AdController. Принят: \n" +
                "(int) id " + id);
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
    public ResponseEntity<?> updateAds(@PathVariable int id,
                                       @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Метод addAds, класса AdController. Приняты: \n" +
                "(int) id " + id +
                "\nНовое объявление или обновление имеющегося " + createOrUpdateAd.toString());
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
        log.info("Метод getAdsCurrentUser, класса AdController.");
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
        log.info("Метод addAds, класса AdController. Приняты: \n" +
                "(int) id " + id +
                "\nИзображение объявления" + image.getOriginalFilename());
        return ResponseEntity.ok().build();
    }
}
