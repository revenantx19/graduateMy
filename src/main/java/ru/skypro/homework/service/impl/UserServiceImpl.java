package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.MyUserDetailsManager;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final MyUserDetailsManager manager;
    private final ImageServiceImpl imageService;

    @Value("avatar")
    private String avatarsDir;

    public void setPassword(NewPassword newPassword, String username) {
        log.info("Вошли в метод setPassword сервиса UserServiceImpl\nПриняты данные:" +
                        "старый пароль {} | новый пароль {} | имя пользователя {}",
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword(),
                username);
        String oldPasswordFromDb = userRepository.findPasswordByUsername(username);
        //данный лог нужно будет убрать из-за содержания пароля,
        // но это не точно, не знаю как правильнее
        log.info("Получен хешированный пароль из БД по имени пользователя.\n" +
                "Хешированный пароль из БД: {}", oldPasswordFromDb);
        // 17.11.2024 добавлена проверка, что новый пароль не совпадает со старым
        // фронт не обладает возможности отправить уведомление об
        // также добавлена проверка, чтобы пароль был не менее 8 символов
        // иначе можно сменить пароль и больше никогда не войти,
        // так как фронт запрашивает пароль от 8 символов при авторизации
        if (encoder.matches(newPassword.getCurrentPassword(), oldPasswordFromDb) && // сравниваем старый со старым из бд
                (!encoder.matches(newPassword.getNewPassword(), oldPasswordFromDb)) && // сравниваем новый со старым из бд
                (newPassword.getNewPassword().length() >= 8))  { // проверяем длину (не менее 8 символов)
            log.info("Требования для смены пароля выполнены");
            userRepository.changePassword(encoder.encode(newPassword.getNewPassword()), username);
            log.info("Пароль был успешно изменён");
        } else {
            throw new SecurityException("Пароль не соответствует требованиям");
        }
    }

    @Override
    public void getUser(UserEntity user) {

    }

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

    @Override
    public void updateUserImage(MultipartFile image, String username) {
        log.info("Вошли в метод updateUserImage сервиса UserServiceImpl " +
                "получен объект: " + image.toString());
        imageService.saveImage(image, username);
    }
}
