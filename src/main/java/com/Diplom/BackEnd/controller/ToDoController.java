package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.MapperToToDoDTOService;
import com.Diplom.BackEnd.service.ToDoService;
import com.Diplom.BackEnd.service.imp.MapperToToDoDTOServiceImpl;
import com.Diplom.BackEnd.service.imp.ToDoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/toDo")
@Slf4j
public class ToDoController {
    @Autowired
    private ToDoService toDoService;
    @Autowired
    private MapperToToDoDTOService mapperToToDoDTOService;

    @GetMapping("author/{id}")
    public ResponseEntity<?> getToDos(@PathVariable(name = "id") Long id){
        try{
            List<ToDoDTO> foundToDoesDTO = mapperToToDoDTOService.mapToToDoDTO(toDoService.getToDos(id));
            return ResponseEntity.ok().body(foundToDoesDTO);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getToDoesCurrentUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @GetMapping("author/current")
    public ResponseEntity<?> getToDoesCurrentUser(@AuthenticationPrincipal User currentUsers){
        try{
            List<ToDoDTO> foundToDoesDTO = mapperToToDoDTOService.mapToToDoDTO(toDoService.getToDos(currentUsers.getId()));
            return ResponseEntity.ok().body(foundToDoesDTO);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getToDoesCurrentUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PostMapping("create")
    public ResponseEntity<?> createToDo(@RequestBody ToDoDTO toDoDTO, @AuthenticationPrincipal User currentUsers){
        try{
            ToDoDTO createdToDoDTO = mapperToToDoDTOService.mapToToDoDTO(toDoService.addToDo(currentUsers.getId(), toDoDTO));
            return ResponseEntity.ok().body(createdToDoDTO);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getToDoesCurrentUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deleteToDo(@PathVariable(name = "id") Long id){
        try{
            toDoService.deleteToDo(id);
            return ResponseEntity.ok().build();
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getToDoesCurrentUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<?> editToDo(@PathVariable(name = "id") Long sourceToDoId, @RequestBody ToDoDTO toDoDTO){
        try{
            ToDoDTO editedToDoDTO = mapperToToDoDTOService.mapToToDoDTO(toDoService.editToDo(sourceToDoId, toDoDTO));
            return ResponseEntity.ok().body(editedToDoDTO);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getToDoesCurrentUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
}
