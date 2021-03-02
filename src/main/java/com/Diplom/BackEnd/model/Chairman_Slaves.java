package com.Diplom.BackEnd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "chairman_slaves")
@Data
@NoArgsConstructor
public class Chairman_Slaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User chairman;

    @OneToMany
    private Set<User> slaves;
}
