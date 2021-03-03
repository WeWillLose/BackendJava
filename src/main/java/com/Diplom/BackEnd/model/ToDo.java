package com.Diplom.BackEnd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "toDo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public ToDo(Long id, String title, String description, String text) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
    }
}
