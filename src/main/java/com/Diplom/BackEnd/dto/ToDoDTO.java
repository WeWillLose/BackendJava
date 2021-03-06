package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.model.ToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoDTO {
    private Long id;
    private String title;
    private String description;
    private String text;

}
