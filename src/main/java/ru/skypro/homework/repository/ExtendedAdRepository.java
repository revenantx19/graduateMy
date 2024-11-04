package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.ExtendedAdDTO;

public interface ExtendedAdRepository extends JpaRepository<ExtendedAdDTO, Long> {

}
