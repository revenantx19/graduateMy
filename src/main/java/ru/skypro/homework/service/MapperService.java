package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.User;

public interface MapperService {

    User getUser(Long id);

    Login getLogin(String username);

    Comment getComment(Long id);

    ExtendedAd getExtendedAd(Long id);

}
