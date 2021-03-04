package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.ToDo;
import com.sun.xml.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MapperToDoDTOServiceImpl {

    public ToDoDTO mapToToDoDTO(ToDo toDo){
        if(toDo == null){
            log.error("In mapToToDoDTO toDo must not be null");
            throw new NullPointerExceptionImpl("In mapToToDoDTO toDo must not be null");
        }
        ToDoDTO toDoDTO = new ToDoDTO(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.getText());
        log.info("IN mapToToDoDTO toDoDTO: {}, mapped to toDo: {}",toDo,toDoDTO);
        return toDoDTO;
    }
    public ToDo mapToToDo(ToDoDTO toDoDTO){
        if(toDoDTO == null){
            log.error("In mapToToDoDTO toDoDTO must not be null");
            throw new NullPointerExceptionImpl("In mapToToDoDTO toDoDTO must not be null");
        }
        ToDo toDo = new ToDo(toDoDTO.getId(), toDoDTO.getTitle(), toDoDTO.getDescription(), toDoDTO.getText());
        log.info("IN mapToToDoDTO toDoDTO: {}, mapped to toDo: {}",toDoDTO,toDo);
        return toDo;
    }
}
