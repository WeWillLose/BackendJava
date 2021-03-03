package com.Diplom.BackEnd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chairman_slaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chairman_Slaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User chairman;

    @OneToMany
    private Set<User> slaves = new HashSet<>();

    public Chairman_Slaves(User chairman, Set<User> slaves) {
        this.chairman = chairman;
        this.slaves = slaves;
    }
}
