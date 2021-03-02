package com.Diplom.BackEnd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 660615173241545375L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    @Override
    public String getAuthority() {
        return name.toString();
    }

    public Role(ERole name) {
        this.name = name;
    }
}
