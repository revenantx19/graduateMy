package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

@Service
public class UserMapper {

    // Маппинг из Register в UserEntity
    public UserEntity toUserEntity(Register register) {
        if (register == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(register.getUsername());
        userEntity.setPassword(register.getPassword()); //надо хешировать
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setPhone(register.getPhone());
        userEntity.setRole(register.getRole());
        //передача ссылки на аватар - не реализовал, но при регистрации сразу его добавить нельзя
        return userEntity;
    }

    // Маппинг из UserEntity в User DTO
    public User toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        User userDto = new User();
        userDto.setEmail(userEntity.getUsername()); //в dto называние поля email
        userDto.setFirstName(userEntity.getFirstName()); //имя
        userDto.setLastName(userEntity.getLastName()); //фамилия
        userDto.setPhone(userEntity.getPhone()); //телефон
        userDto.setRole(userEntity.getRole()); //роль

        return userDto;
    }

}