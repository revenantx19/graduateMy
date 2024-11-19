package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageEntity;

import java.io.IOException;

public interface ImageService {

    String getExtension(MultipartFile file);

    String saveImageToDisk(MultipartFile file, String username) throws IOException;

    void createImageEntity(MultipartFile file, String username) throws IOException;

    void uploadImage(MultipartFile file, String username) throws IOException;


}
