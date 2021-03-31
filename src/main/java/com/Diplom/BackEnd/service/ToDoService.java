package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.model.ToDo;

import java.util.List;

public interface ToDoService {

    List<ToDo> getToDos(Long authorId);

    ToDo editToDo(Long sourceToDoId, ToDoDTO changedToDoDTO);

    ToDo editToDo(Long sourceToDoId, ToDo changedToDo);

    ToDo addToDo(Long authorId, ToDoDTO toDo);

    ToDo addToDo(Long authorId, ToDo toDo);

    boolean existsById(Long id);

    void deleteToDo(Long toDoId);
}
