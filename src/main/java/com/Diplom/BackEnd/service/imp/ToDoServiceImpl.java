package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ValidationErrorImpl;
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
public class ToDoServiceImpl {
    @Autowired
    private ToDoRepo toDoRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MapperToDoDTOServiceImpl mapperToDoDTOService;

    public ToDoDTO mapToToDoDTO(ToDo toDo){
       return mapperToDoDTOService.mapToToDoDTO(toDo);
    }
    public List<ToDoDTO> mapToToDoDTO(List<ToDo> toDoes){
        if(toDoes == null){
            throw new NullPointerExceptionImpl("toDoes must not be null");
        }
        return toDoes.stream().map(mapperToDoDTOService::mapToToDoDTO).collect(Collectors.toList());
    }
    public List<ToDo> mapToToDo(List<ToDoDTO> toDoesDTO){
        if(toDoesDTO == null){
            throw new NullPointerExceptionImpl("toDoesDTO must not be null");
        }
        return toDoesDTO.stream().map(mapperToDoDTOService::mapToToDo).collect(Collectors.toList());
    }
    public ToDo mapToToDo(ToDoDTO toDoDTO){
        return mapperToDoDTOService.mapToToDo(toDoDTO);
    }

    public  List<ToDo> getToDoes(Long id){
        if(id == null){
            log.error("IN getToDoes id is null");
            throw new NullPointerExceptionImpl("IN getToDoes id must not be null");
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

    public ToDo editToDo(ToDo sourceToDo,ToDoDTO changedToDoDTO){
       return editToDo(sourceToDo,mapToToDo(changedToDoDTO));
    }

    public ToDo editToDo(ToDo sourceToDo,ToDo changedToDo){
        if(sourceToDo == null){
            throw new NullPointerExceptionImpl("IN editToDo sourceToDo must not be null");
        }
        if(changedToDo == null){
            throw new NullPointerExceptionImpl("IN editToDo sourceToDo must not be null");
        }
        if(sourceToDo.getId() == null){
            throw new ValidationErrorImpl("id должен быть не пустым");
        }
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
        if(author == null){
            throw new NullPointerExceptionImpl("IN addToDo author must not be null");
        }
        if(toDo == null){
            throw new NullPointerExceptionImpl("IN addToDo toDo must not be null");
        }
        if(author.getId() == null){
            throw new NullPointerExceptionImpl("IN addToDo author.id must not be null");
        }
        if(!userService.existsById(author.getId())){
            throw new UserNotFoundExceptionImpl();
        }
        toDo.setAuthor(author);
        return  toDoRepo.save(toDo);

    }

    public boolean existsById(Long id){
        if(id == null){
            throw new NullPointerExceptionImpl("IN existsById id must not be null");
        }
        return toDoRepo.existsById(id);
    }

    public void deleteToDo(ToDo toDo){
        if(toDo == null){
            throw new NullPointerExceptionImpl("IN deleteToDo toDo must not be null");
        }
        if(toDo.getId() == null){
            throw new NullPointerExceptionImpl("IN deleteToDo toDo.id must not be null");
        }
        if(!this.existsById(toDo.getId())){
            throw new UserNotFoundExceptionImpl();
        }
        toDoRepo.delete(toDo);
    }
}
