package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.UserDTO;

public interface UserRepository extends JpaRepository<UserDTO, Long> {

}
