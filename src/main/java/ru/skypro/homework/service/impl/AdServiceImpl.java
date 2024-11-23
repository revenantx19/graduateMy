package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public Ads getAllAds() {
        List<Ad> adsList = adRepository.findAll().stream()
                .map(adEntity -> adMapper.toAdDto(adEntity))
                .collect(Collectors.toList());
        return new Ads(adsList.size(), adsList.toArray(Ad[]::new));
    }

    public Ad addAd(CreateOrUpdateAd createAd,
                      MultipartFile adImage,
                      String username) throws IOException {
        log.info("Вошли в метод addAd сервиса AdServiceImpl \n" +
                "Получены данные (объект) createAD: " + createAd +
                "\nФайл объявления " + adImage.getOriginalFilename() +
                "\nИмя авторизированного пользователя: " + username);

        UUID uuid = UUID.randomUUID();
        String filePathString = "/ad_images/" + uuid + "." + getExtension(adImage);
        Path filePath = Path.of("ad_images", uuid + "." + getExtension(adImage));

        Files.createDirectories((filePath.getParent()));

        try (InputStream is = adImage.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            log.info("Картинка объявления успешно сохранёна на диск." +
                    "\nПолное имя файла: " + filePathString);
        }
        AdEntity adEntity = adMapper.toAdEntity(createAd, filePathString, username);
        log.info("Использован маппер, получена сущность: {}", adEntity);
        adRepository.save(adEntity);
        log.info("Сущность сохранена в БД успешно");

        AdEntity adEntityFromBd = adRepository.findAdEntityByImage(filePathString);
        Ad adMapping = adMapper.toAdDto(adEntityFromBd);

        return adMapping;

    }

    /**
     * Метод извлекающий расширение переданного файла
     * @param file переданный мультимедиа файл
     * @return тип файла, например "jpg"
     */
    public String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }

    public Ads getMyAds(Authentication authentication) {
        List<Ad> myAdsList = adRepository.findAll().stream()
                .filter(adEntity -> adEntity.getUser().getUsername().equals(authentication.getName()))
                .map(adEntity -> adMapper.toAdDto(adEntity))
                .collect(Collectors.toList());
        return new Ads(myAdsList.size(), myAdsList.toArray(Ad[]::new));
    }

    public byte[] findAdImageByFilename(String fileName) throws IOException {
        log.info("Вошли в метод findAvatarImageByFilename сервиса AdServiceImpl " +
                "\nполучен fileName (String): " + fileName);
        return Files.readAllBytes(Path.of("ad_images/" + fileName));
    }

    public ExtendedAd getAdById(int id) {

        AdEntity adEntity = adRepository.findAdEntityById(id);

        return adMapper.toExtendedAd(adEntity);
    }
}
