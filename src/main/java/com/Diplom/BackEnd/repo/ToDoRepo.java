package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepo extends JpaRepository<ToDo,Long> {
    List<ToDo> findByAuthor(User user);
}
