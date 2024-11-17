package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String getExtension(MultipartFile file);

    String saveImage(MultipartFile file, String username);


}
