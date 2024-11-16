package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

@Service
@Slf4j
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserDetailsManager manager, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.manager = manager;
        this.encoder = encoder;
    }



    public void saveUserEntity(Register register) {
        log.info("Вы вошли в метод saveUserEntity");
        userRepository.save(userMapper.toUserEntity(register));
    }



    public void setPassword(NewPassword newPassword) {

    }
}
