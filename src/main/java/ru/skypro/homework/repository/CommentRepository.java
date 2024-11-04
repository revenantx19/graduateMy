package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.CommentDTO;

public interface CommentRepository extends JpaRepository<CommentDTO, Long> {

}
