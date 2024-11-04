package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.LoginDTO;

public interface LoginRepository extends JpaRepository<LoginDTO, Long> {

}
