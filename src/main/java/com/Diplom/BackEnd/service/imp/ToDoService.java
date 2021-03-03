package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ToDoRepo;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ToDoService {
    @Autowired
    private ToDoRepo toDoRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MapperToDoDTOServiceImpl mapperToDoDTOService;

    public ToDoDTO mapToToDoDTO(ToDo toDo){
       return mapperToDoDTOService.mapToToDoDTO(toDo);
    }
    public List<ToDoDTO> mapToToDoDTO(List<ToDo> toDo){
        return toDo.stream().map(mapperToDoDTOService::mapToToDoDTO).collect(Collectors.toList());
    }
    public List<ToDo> mapToToDo(List<ToDoDTO> toDoDTO){
        return toDoDTO.stream().map(mapperToDoDTOService::mapToToDo).collect(Collectors.toList());
    }
    public ToDo mapToToDo(ToDoDTO toDoDTO){
        return mapperToDoDTOService.mapToToDo(toDoDTO);
    }

    public  List<ToDo> getToDoes(Long id){
        if(id == null){
            log.error("IN getToDoes id is null");
            throw new NullPointerException("IN getToDoes id must not be null");
        }
        User byId = userService.findById(id);
        return getToDoes(byId);
    }

    public List<ToDo> getToDoes(User user){
        if(!userService.existsByUsername(user.getUsername())){
            log.error("IN getToDoes user: {} doesnt exists",user);
            throw new UserNotFoundExceptionImpl();
        }
        List<ToDo> byAuthor = toDoRepo.findByAuthor(user);
        log.info("IN getToDoes found: {} by author {}",byAuthor,user);
        return byAuthor;
    }

    public ToDo editToDo(Long id,ToDo changedToDo){
        ToDo toDo = toDoRepo.findById(id).orElseThrow(UserNotFoundExceptionImpl::new);
        return editToDo(toDo,changedToDo);
    }
    public ToDo editToDo(ToDo sourceToDo,ToDoDTO changedToDoDTO){
       return editToDo(sourceToDo,mapToToDo(changedToDoDTO));
    }

    public ToDo editToDo(ToDo sourceToDo,ToDo changedToDo){
        if(changedToDo.getTitle() !=null && !changedToDo.getTitle().isBlank()  ){
            sourceToDo.setTitle(changedToDo.getTitle());
        }
        if(changedToDo.getText() !=null && !changedToDo.getText().isBlank()){
            sourceToDo.setText(changedToDo.getText());
        }
        if(changedToDo.getDescription() !=null && !changedToDo.getDescription().isBlank()){
            sourceToDo.setDescription(changedToDo.getDescription());
        }
        return toDoRepo.save(sourceToDo);
    }

    public ToDo addToDo(User author,ToDo toDo){
        toDo.setAuthor(author);
        return  toDoRepo.save(toDo);

    }
    public void deleteToDo(ToDo toDo){
        toDoRepo.delete(toDo);

    }
}
