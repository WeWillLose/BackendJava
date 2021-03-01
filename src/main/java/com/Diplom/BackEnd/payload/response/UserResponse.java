package com.Diplom.BackEnd.payload.response;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String patronymic;

    private Set<RoleResponse> roles = new HashSet<>();

    private UserResponse chairman;

    private Set<UserResponse> slaves = new HashSet<>();

    private boolean is_chairman;


    public boolean isIs_chairman() {
        return roles.contains(new Role(ERole.ROLE_CHAIRMAN));
    }

    public UserResponse(Long id, String username, String firstName, String lastName, String patronymic, Set<Role> roles, User chairman, Set<User> slaves) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.chairman = new UserResponse(chairman);
        this.slaves = slaves.stream().map(UserResponse::new).collect(Collectors.toSet());
        this.roles = roles.stream().map(t -> new RoleResponse(t.getName().name())).collect(Collectors.toSet());
    }

    public UserResponse(User user, User chairman, Set<User> slaves) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.patronymic = user.getPatronymic();
        this.roles = user.getRoles().stream().map(t -> new RoleResponse(t.getName().name())).collect(Collectors.toSet());
        this.chairman = new UserResponse(chairman);
        this.slaves = slaves.stream().map(UserResponse::new).collect(Collectors.toSet());
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.patronymic = user.getPatronymic();
        this.roles = user.getRoles().stream().map(t -> new RoleResponse(t.getName().name())).collect(Collectors.toSet());
    }

}
