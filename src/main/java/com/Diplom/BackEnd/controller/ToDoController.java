package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;
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
    private ToDoServiceImpl toDoServiceImpl;

    @GetMapping("author/{id}")
    public ResponseEntity<?> getToDoes(@PathVariable(name = "id") User user){
        try{
            List<ToDoDTO> foundToDoesDTO = toDoServiceImpl.mapToToDoDTO(toDoServiceImpl.getToDoes(user));
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
            List<ToDoDTO> foundToDoesDTO = toDoServiceImpl.mapToToDoDTO(toDoServiceImpl.getToDoes(currentUsers));
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
    public ResponseEntity<?> createToDo(@RequestBody ToDo toDo, @AuthenticationPrincipal User currentUsers){
        try{
            ToDoDTO createdToDoDTO = toDoServiceImpl.mapToToDoDTO(toDoServiceImpl.addToDo(currentUsers, toDo));
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
    @PreAuthorize("#toDo.author.id == authentication.principal.id")
    public ResponseEntity<?> deleteToDo(@PathVariable(name = "id") ToDo toDo){
        try{
            toDoServiceImpl.deleteToDo(toDo);
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
    @PreAuthorize("#toDo.author.id == authentication.principal.id")
    public ResponseEntity<?> editToDo(@PathVariable(name = "id") ToDo toDo, @RequestBody ToDoDTO toDoDTO){
        try{
            ToDoDTO editedToDoDTO = toDoServiceImpl.mapToToDoDTO(toDoServiceImpl.editToDo(toDo, toDoDTO));
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
