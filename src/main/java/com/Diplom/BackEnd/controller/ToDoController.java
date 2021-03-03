package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ToDoRepo;
import com.Diplom.BackEnd.service.imp.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/toDo")
public class ToDoController {
    @Autowired
    private ToDoRepo toDoRepo;

    @Autowired
    private ToDoService toDoService;

    @GetMapping("author/{id}")
    public ResponseEntity<?> getToDoes(@PathVariable(name = "id") User user){
        return ResponseEntity.ok(toDoService.mapToToDoDTO(toDoService.getToDoes(user)));
    }
    @GetMapping("author/current")
    public ResponseEntity<?> getToDoesCurrentUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(toDoService.mapToToDoDTO(toDoService.getToDoes(user)));
    }

    @PostMapping("create")
    public ResponseEntity<?> createToDo(@RequestBody ToDo toDo, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(toDoService.mapToToDoDTO(toDoService.addToDo(user,toDo)));
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("#toDo.author.id == authentication.principal.id")
    public ResponseEntity<?> deleteToDo(@PathVariable(name = "id") ToDo toDo){
        toDoService.deleteToDo(toDo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("edit/{id}")
    @PreAuthorize("#toDo.author.id == authentication.principal.id")
    public ResponseEntity<?> editToDo(@PathVariable(name = "id") ToDo toDo, @RequestBody ToDoDTO toDoDTO){

        return ResponseEntity.ok().body(toDoService.mapToToDoDTO(toDoService.editToDo(toDo,toDoDTO)));
    }
}
