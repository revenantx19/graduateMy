package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class ImageServiceImpl implements ImageService {
    @Override
    public String getExtension(MultipartFile file) {
        log.info("Вошли в метод getExtension сервиса ImageServiceImpl\n" +
                "Получен файл для сохранения " + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }

    @Override
    public String saveImage(MultipartFile file, String username) {
        log.info("Вошли в метод saveImage сервиса ImageServiceImpl\n" +
                "Получен файл для сохранения " + file.getOriginalFilename());




        return "";
    }
}
