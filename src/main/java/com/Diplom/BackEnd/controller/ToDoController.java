package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerExceptionImpl;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.ToDoMapperService;
import com.Diplom.BackEnd.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private ToDoMapperService toDoMapperService;

    @GetMapping("author/{id}")
    public ResponseEntity<?> getToDos(@PathVariable(name = "id") Long id){
        try{
            return ResponseEntity.ok().body(toDoMapperService.mapToToDoDTO(toDoService.getToDos(id)));
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @GetMapping("author/current")
    public ResponseEntity<?> getToDoesCurrentUser(@AuthenticationPrincipal User currentUsers){
        try{
            return ResponseEntity.ok().body(toDoMapperService.mapToToDoDTO(toDoService.getToDos(currentUsers.getId())));
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createToDo(@RequestBody ToDoDTO toDoDTO, @AuthenticationPrincipal User currentUsers){
        try{
            return ResponseEntity.ok().body(toDoMapperService.mapToToDoDTO(toDoService.addToDo(currentUsers.getId(), toDoDTO)));
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteToDo(@PathVariable(name = "id") Long id){
        try{
            toDoService.deleteToDo(id);
            return ResponseEntity.ok().build();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editToDo(@PathVariable(name = "id") Long sourceToDoId, @RequestBody ToDoDTO toDoDTO){
        try{
            ToDoDTO editedToDoDTO = toDoMapperService.mapToToDoDTO(toDoService.editToDo(sourceToDoId, toDoDTO));
            return ResponseEntity.ok().body(editedToDoDTO);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
}
