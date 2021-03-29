package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.model.ToDo;

import java.util.List;

public interface ToDoMapperService {
     ToDoDTO mapToToDoDTO(ToDo toDo);
     List<ToDoDTO> mapToToDoDTO(List<ToDo> toDos);
     ToDo mapToToDo(ToDoDTO toDoDTO);
     List<ToDo> mapToToDo(List<ToDoDTO> toDosDTO);
}
