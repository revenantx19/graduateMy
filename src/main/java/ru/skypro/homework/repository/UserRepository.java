package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.User;

public interface UserRepository extends JpaRepository<User, Long> {

}