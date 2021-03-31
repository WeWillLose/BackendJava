package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ToDoDTO;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ForbiddenExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ToDoNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ToDoRepo;
import com.Diplom.BackEnd.service.CanEditService;
import com.Diplom.BackEnd.service.ToDoMapperService;
import com.Diplom.BackEnd.service.ToDoService;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ToDoServiceImpl implements ToDoService {
    @Autowired
    private ToDoRepo toDoRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ToDoMapperService toDoMapperService;

    @Autowired
    CanEditService canEditService;

    public  List<ToDo> getToDos(Long authorId){
        if(authorId == null){
            log.error("IN getToDos authorId is null");
            throw new NullPointerExceptionImpl("IN getToDos authorId must not be null");
        }
        User byId = userService.findById(authorId);

        if(byId == null){
            throw new UserNotFoundExceptionImpl(authorId);
        }

        List<ToDo> reportsByAuthor = toDoRepo.findByAuthor(byId);

        if(reportsByAuthor == null){
            log.error("IN getToDos reportsByAuthor is null");
            throw new NullPointerException("IN getToDos reportsByAuthor is null");
        }
        log.info("IN getToDoes found: {} by author {}",reportsByAuthor,byId);
        return reportsByAuthor;
    }


    public ToDo editToDo(Long sourceToDoId,ToDoDTO changedToDoDTO){
       return editToDo(sourceToDoId,toDoMapperService.mapToToDo(changedToDoDTO));
    }

    public ToDo editToDo(Long sourceToDoId,ToDo changedToDo){
        if(sourceToDoId == null){
            throw new NullPointerExceptionImpl("IN editToDo sourceToDo must not be null");
        }
        if(changedToDo == null){
            throw new NullPointerExceptionImpl("IN editToDo sourceToDo must not be null");
        }

        ToDo toDo = toDoRepo.findById(sourceToDoId).orElse(null);

        if(toDo==null){
            throw new ToDoNotFoundExceptionImpl(sourceToDoId);
        }

        if(!canEditService.canEdit(toDo.getAuthor())){
            throw new ForbiddenExceptionImpl();
        }

        if(changedToDo.getTitle() != null && !changedToDo.getTitle().isBlank()){
            toDo.setTitle(changedToDo.getTitle());
        }

        if(changedToDo.getText() !=null && !changedToDo.getText().isBlank()){
            toDo.setText(changedToDo.getText());
        }

        if(changedToDo.getDescription() !=null && !changedToDo.getDescription().isBlank()){
            toDo.setDescription(changedToDo.getDescription());
        }

        return toDoRepo.save(toDo);
    }
    public ToDo addToDo(Long authorId,ToDoDTO toDo){
        return addToDo(authorId,toDoMapperService.mapToToDo(toDo));
    }
    public ToDo addToDo(Long authorId,ToDo toDo){
        if(toDo == null){
            throw new NullPointerExceptionImpl("IN addToDo toDo must not be null");
        }
        if(authorId == null){
            throw new NullPointerExceptionImpl("IN addToDo author.id must not be null");
        }
        User byId = userService.findById(authorId);

        if(byId == null){
            throw new UserNotFoundExceptionImpl(authorId);
        }
        toDo.setAuthor(byId);
        return  toDoRepo.save(toDo);
    }

    public boolean existsById(Long id){
        if(id == null){
            throw new NullPointerExceptionImpl("IN existsById id must not be null");
        }
        return toDoRepo.existsById(id);
    }

    public void deleteToDo(Long toDoId){

        ToDo toDo = toDoRepo.findById(toDoId).orElse(null);
        if(toDo == null){
            throw new ToDoNotFoundExceptionImpl(toDoId);
        }
        if(!canEditService.canEdit(toDo.getAuthor())){
            throw new ForbiddenExceptionImpl();
        }
        toDoRepo.delete(toDo);
    }
}
