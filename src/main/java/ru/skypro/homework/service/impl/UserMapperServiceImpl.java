package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

@Service
@Slf4j
public class UserMapperServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserMapperServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void saveUserEntity(Register register) {
        log.info("Вы вошли в метод saveUserEntity");
        userRepository.save(userMapper.toUserEntity(register));
    }

    public User getUserDto() {

        User user = new User();
        return user;

    }
}
