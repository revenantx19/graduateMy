package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    /**
     * Метод обновления пароля в базе данных
     * @param newPassword объект из контроллера содержащий новый и старый пароль
     * @param username строка с логином (email) авторизированного пользователя
     */
    public void setPassword(NewPassword newPassword, String username) {
        log.info("Вошли в метод setPassword сервиса UserServiceImpl\nПриняты данные:" +
                        "старый пароль {} | новый пароль {} | имя пользователя {}",
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword(),
                username);
        String oldPasswordFromDb = userRepository.findPasswordByUsername(username);
        log.info("Получен хешированный пароль из БД по имени пользователя.\n" +
                "Хешированный пароль из БД: {}", oldPasswordFromDb);
        if (encoder.matches(newPassword.getCurrentPassword(), oldPasswordFromDb) &&
                (!encoder.matches(newPassword.getNewPassword(), oldPasswordFromDb)) &&
                (newPassword.getNewPassword().length() >= 8))  {
            log.info("Требования для смены пароля выполнены");
            userRepository.changePassword(encoder.encode(newPassword.getNewPassword()), username);
            log.info("Пароль был успешно изменён");
        } else {
            throw new SecurityException("Пароль не соответствует требованиям");
        }
    }

    /**
     * Метод передающий в контроллер информацию о юзере
     * @param username строка содержащая имя текущего пользователя
     * @return userDto - информация о юзере
     */
    @Override
    public User getUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        User userDto = userMapper.toUserDto(userEntity);
        return userDto;
    }

    /**
     * Метод обновления данных о пользователе
     * @param updateUser объект содержащий имя, фамилию и телефон
     * @param username строка с именем авторизованного пользователя
     */
    @Override
    public void updateUser(UpdateUser updateUser, String username) {
        log.info("Вошли в метод updateUser сервиса UserServiceImpl " +
                "получен объект: " + updateUser);
        userRepository.changeUserData(updateUser.getFirstName(),
                                      updateUser.getLastName(),
                                      updateUser.getPhone(),
                                      username);
        log.info("Смена данных пользователя успешно завершена");
    }

    /**
     * Метод первичного добавления или обновления аватарки пользователя
     * который сохраяет переданное изображение на диск
     * @param avatarImage принимаемый файл с изображением
     * @param username строка с именем авторизованного пользователя
     * @throws IOException исключение работы с файлами
     */
    @Override
    public void updateUserAvatar(MultipartFile avatarImage, String username) throws IOException {
        log.info("Вошли в метод updateUserAvatar сервиса UserServiceImpl " +
                "получено изображение: " + avatarImage.getOriginalFilename());
        UUID uuid = UUID.randomUUID();
        String filePathString = "/avatar/" + uuid + "." + getExtension(avatarImage);
        Path filePath = Path.of("avatar", uuid + "." + getExtension(avatarImage));

        Files.createDirectories((filePath.getParent()));

        try (InputStream is = avatarImage.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            log.info("Файл успешно сохранён на диск." +
                    "\nПолное имя файла: " + filePathString);
        }
        userRepository.saveAvatarPath(filePathString, username);
        log.info("Путь картинки сохранён в столбец image, таблицы app_user");
    }

    /**
     * Метод для передачи контроллеру массив байт аватарки
     * @param fileName имя файла из контроллера
     * @return массив байт byte[]
     * @throws IOException
     */
    @Override
    public byte[] findAvatarImageByFilename(String fileName) throws IOException {
        log.info("Вошли в метод findAvatarImageByFilename сервиса UserServiceImpl " +
                "\nполучен fileName (String): " + fileName);
        return Files.readAllBytes(Path.of("avatar/" + fileName));

    }
    /**
     * Метод извлекающий расширение переданного файла
     * @param file переданный мультимедиа файл
     * @return тип файла, например "jpg"
     */
    @Override
    public String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }
}
