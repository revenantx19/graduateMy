package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;

public interface UserService {

    void setPassword(NewPassword newPassword, String username);

    User getUser(String username);

    void updateUser(UpdateUser updateUser, String username);

    void updateUserAvatar(MultipartFile image, String username) throws IOException;
    String getExtension(MultipartFile file);
    byte[] findAvatarImageByFilename(String fileName) throws IOException;
}
