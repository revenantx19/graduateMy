package ru.skypro.homework.service;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.UserEntity;

public interface UserMapperService {

    UserEntity toUserEntity(Register register);

    User toUserDto(UserEntity userEntity);

}
