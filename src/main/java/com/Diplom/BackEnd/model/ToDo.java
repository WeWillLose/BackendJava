package com.Diplom.BackEnd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "toDo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ToDo  extends SuperClass<String>  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    public ToDo(Long id, String title, String description, String text) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
    }
}
