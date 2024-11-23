package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@Tag(name = "Объявления")
@RequiredArgsConstructor
public class AdController {
    private final AdServiceImpl adServiceImpl;
    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @GetMapping(path = "/ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))
            }),
    })
    public ResponseEntity<Ads> getAllAds() {
        log.info("Метод getAllAds, класса AdController");
        return new ResponseEntity<>(adServiceImpl.getAllAds(), HttpStatus.OK);
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
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                   @RequestPart(value = "image", required = true) MultipartFile image,
                                   Authentication authentication) throws IOException {
        log.info("Метод addAds, класса AdController. Приняты: \n" +
                "Новое объявление или обновление имеющегося " + properties.toString() +
                "\nИзображение объявления" + image.getOriginalFilename());
        Ad ad = adServiceImpl.addAd(properties, image, authentication.getName());
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(ad, HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExtendedAd.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<ExtendedAd> getAdsById(@PathVariable int id) {
        log.info("Метод getAdsById, класса AdController. Принят: \n" +
                "(int) id " + id);
        return ResponseEntity.ok(adServiceImpl.getAdById(id));
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
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Метод getAdsCurrentUser, класса AdController.");
        return ResponseEntity.ok(adServiceImpl.getMyAds(authentication));
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

    @GetMapping(value = "/ad_images/{fileName}",
            produces = {MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getAdImageByFilename(@PathVariable String fileName) throws IOException {
        log.info("Вошли в метод getAdImageByFilename, класса UserController.");
        byte[] adImageData = adServiceImpl.findAdImageByFilename(fileName);
        log.info("Получен массив байт (выведем первый байт): {}", adImageData[0]);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_GIF)
                .body(adImageData);
    }

}
