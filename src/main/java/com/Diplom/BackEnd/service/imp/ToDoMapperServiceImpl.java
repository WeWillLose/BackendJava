package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.service.ToDoMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ToDoMapperServiceImpl implements ToDoMapperService {

    public ToDoDTO mapToToDoDTO(ToDo toDo){
        if(toDo == null){
            log.error("In mapToToDoDTO toDo is null");
            return null;
        }
        ToDoDTO toDoDTO = new ToDoDTO(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.getText());
        log.info("IN mapToToDoDTO toDoDTO: {}, mapped to toDo: {}",toDo,toDoDTO);
        return toDoDTO;
    }
    public List<ToDoDTO> mapToToDoDTO(List<ToDo> toDos){
        if(toDos == null){
            log.error("In mapToToDoDTO toDos must not be null");
            throw new NullPointerExceptionImpl("In mapToToDoDTO toDos must not be null");
        }
        return toDos.stream().map(this::mapToToDoDTO).collect(Collectors.toList());
    }
    public ToDo mapToToDo(ToDoDTO toDoDTO){
        if(toDoDTO == null){
            log.error("In mapToToDoDTO toDoDTO is null");
            return null;
        }
        ToDo toDo = new ToDo(toDoDTO.getId(), toDoDTO.getTitle(), toDoDTO.getDescription(), toDoDTO.getText());
        log.info("IN mapToToDoDTO toDoDTO: {}, mapped to toDo: {}",toDoDTO,toDo);
        return toDo;
    }

    public List<ToDo> mapToToDo(List<ToDoDTO> toDosDTO){
        if(toDosDTO == null){
            log.error("In mapToToDo toDosDTOis null");
            return null;
        }
        return toDosDTO.stream().map(this::mapToToDo).collect(Collectors.toList());
    }
}
