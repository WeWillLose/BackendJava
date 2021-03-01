package com.Diplom.BackEnd.model;

import com.Diplom.BackEnd.payload.jsonView.AdminResponse;
import com.Diplom.BackEnd.payload.jsonView.Response;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator",initialValue = 1,allocationSize = 5,sequenceName = "user_id_sequence")
    private Long id;
    @JsonView(Response.class)
    @Column(unique = true)
    private String username;
    @JsonView(AdminResponse.class)
    @Column
    private String password;
    @JsonView(Response.class)
    @Column(name = "first_name")
    private String firstName;
    @JsonView(Response.class)
    @Column(name = "last_name")
    private String lastName;
    @JsonView(Response.class)
    @Column
    private String patronymic;

    @JsonView(Response.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public User(String username, String password, String lastName, String firstName, String patronymic, Set<Role> roles, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.roles = roles;
        this.isActive = isActive;
    }
    public User(String username, String password, String lastName, String firstName, String patronymic, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.roles = roles;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return getIsActive();
    }
}
