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

    /**
     * Метод получения всех имеющихся объявлений
     *
     * @return объект Ads содержащий массив всех объявлений
     */
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

    /**
     * Метод добавление нового объявления
     *
     * @param properties     объект содержащий название, цену и описание объявления
     * @param image          картинка объявления
     * @param authentication объект, содержащий информацию о текущем авторизованном пользователе
     * @return объект Ad содеражищий информацию о добавленном объявлении
     * @throws IOException
     */
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
        log.info("Метод addAd, класса AdController. Приняты: \n" +
                "Новое объявление или обновление имеющегося " + properties.toString() +
                "\nИзображение объявления" + image.getOriginalFilename());
        Ad ad = adServiceImpl.addAd(properties, image, authentication.getName());
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(ad, HttpStatus.CREATED);
        }
    }

    /**
     * Метод получения информации об объявлении
     * @param id примитив объявления
     * @param authentication объект, содержащий авторизированного пользователя
     * @return объект ExtendedAd с подробной информаций об объявлении и о пользователе его создавшим
     */
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
    public ResponseEntity<ExtendedAd> getAdsById(@PathVariable int id,
                                                 Authentication authentication) {
        log.info("Метод getAdsById, класса AdController. Принят: \n" +
                "(int) id " + id);
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            return ResponseEntity.ok(adServiceImpl.getAdById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Удаление объявления из базы данных
     * @param id объявления
     * @param authentication объект, содержащий данный об авторизированном пользователе
     * @return HttpStatus`ы в соответствии со спецификацией
     */
    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @DeleteMapping(path = "/ads/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<?> removeAdById(@PathVariable("id") int id,
                                          Authentication authentication) {
        log.info("Метод removeAdsById, класса AdController. Принят: \n" +
                "(int) id " + id);
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            adServiceImpl.deleteAdById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Метод обновления информации о пользователе
     * @param id объявления, в которое вносятся изменения
     * @param createOrUpdateAd объект, содержащий новое название, цену и описание для объявления
     * @param authentication объект, содержащий данные авторизованного пользователя
     * @return объект Ad, содержащий объявление с изменениями
     */
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
    public ResponseEntity<Ad> updateAds(@PathVariable int id,
                                        @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                        Authentication authentication) {
        log.info("Метод updateAd, класса AdController. Приняты: \n" +
                "(int) id объявления: " + id +
                "\nНовое объявление или обновление имеющегося " + createOrUpdateAd.toString());
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            Ad ad = adServiceImpl.updateInfoAboutAd(id, createOrUpdateAd);
            log.info("Получен объект Ad обратно в контроллер: " + ad);
            return new ResponseEntity<>(ad, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получение списка объявлений авторизованного пользователя
     * @param authentication объект, содержащий информацию об авторизированном пользователе
     * @return объект Ads содержащий количество объявлений и список объявлений
     */
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
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return ResponseEntity.ok(adServiceImpl.getMyAds(authentication));
        }
    }

    /**
     * Метод обновления картинки объявления
     * @param id объявления
     * @param image новое изображение
     * @param authentication объект, содержащий информацию об авторизированном пользователе
     * @return массив байт byte[] изображения
     * @throws IOException
     */
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
    public ResponseEntity<byte[]> updateImage(@PathVariable("id") int id,
                                         @RequestPart(value = "image", required = true) MultipartFile image,
                                         Authentication authentication) throws IOException {
        log.info("Метод addAds, класса AdController. Приняты: \n" +
                "(int) id " + id +
                "\nИзображение объявления" + image.getOriginalFilename());
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adServiceImpl.existId(id)) {
            byte[] imageArrayBytes = adServiceImpl.updateImageAd(id, image);
            log.info("Получен массив байт в контроллер: " + imageArrayBytes[0]);
            return new ResponseEntity<>(imageArrayBytes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Метод получающий имя изображения находящегося в каталоге /ad_images/
     * @param fileName имя файла с расширением
     * @return массив байт byte[] изображения хранящегося на диске
     * @throws IOException
     */
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
