package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("avatar")
    private String avatarsDir;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Override
    public String getExtension(MultipartFile file) {
        //log.info("Вошли в метод getExtension сервиса ImageServiceImpl\n" +
        //        "Получен файл для сохранения " + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            //log.info("Получен тип файла: " + fileName.substring(fileName.lastIndexOf(".") + 1));
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }

    @Override
    public String saveImageToDisk(MultipartFile file, String username) throws IOException {
        log.info("Вошли в метод saveImageToDisk сервиса ImageServiceImpl\n" +
                "Получен файл для сохранения " + file.getOriginalFilename() +
                "\nВызван метод getExtension для получения типа файла " + getExtension(file));

        Path filePath = Path.of(avatarsDir, username + "." + getExtension(file));

        if (Files.exists(Path.of(avatarsDir + "\\" + username + "." + getExtension(file)))) {
            log.info("Загружаемый файл с таким именем имелся в базе данных, предыдущий файл удалён");
            Files.delete(Path.of(avatarsDir + "\\" + username + "." + getExtension(file)));
        };

        Files.createDirectories((filePath.getParent()));

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        return filePath.toString();
    }

    @Override
    @Transactional
    public void createImageEntity(MultipartFile file, String username) throws IOException {
        log.info("Вошли в метод createImageIntity сервиса ImageServiceImpl\n" +
                "Получен файл для сохранения " + file.getOriginalFilename() +
                "\nВызван метод getExtension для получения типа файла " + getExtension(file));
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setFilePath(saveImageToDisk(file, username));
        imageEntity.setFileSize(file.getSize());
        imageEntity.setMediaType(getExtension(file));
        try {
            byte[] bytes = file.getBytes();
            imageEntity.setData(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageEntity.setUserEntity(userRepository.findByUsername(username));

        /**
         * Если данный метод будет использоваться и для сохранения изображения объявлений
         * то нужно предусмотреть сохранение связи с сущностью.
         * Пока идея такая, что аватарка - есть связь с юзером и всё.
         * Картинка объявления - есть связь с ющером и сущностью объявления....
         */
        imageRepository.save(imageEntity);
        //сохраняем сущность картинки в рамках связи @OneToOne
        userRepository.saveAvatar(imageEntity, username);

        userRepository.saveAvatarPath(imageEntity.getFilePath(), username);
        log.info("Сущность картинки сохранена в БД");
        //return entity.getId();
    }

    @Override
    public void uploadImage(MultipartFile file, String username) throws IOException {

        createImageEntity(file, username);

        //Student student = studentRepository.findById(studentId)
        //.orElseThrow(() -> new RuntimeException("Студент с таким ID не найден"));
    }



}
