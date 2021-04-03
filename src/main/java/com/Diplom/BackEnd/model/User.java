package com.Diplom.BackEnd.model;


import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User  extends SuperClass<String>  implements UserDetails, Serializable {

    private static final long serialVersionUID = 31275976440053607L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator",initialValue = 2,allocationSize = 5,sequenceName = "user_id_sequence")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String patronymic;


    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "chairman_id")
    private User chairman;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany (mappedBy="author", fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude private Set<Report> reports = new HashSet<>();

    @OneToMany (mappedBy="author", fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude  private Set<ToDo> toDos = new HashSet<>();

    @Column(name = "is_active")
    private Boolean isActive = true;

    public Boolean getIsActive(){
        if (isActive == null) return true;
        return isActive;
    }

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

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return getIsActive();
    }
    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return getIsActive();
    }
    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return getIsActive();
    }
    @Transient
    @Override
    public boolean isEnabled() {
        return getIsActive();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public User getChairman() {
        return chairman;
    }

    public void setChairman(User chairman) {
        this.chairman = chairman;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<ToDo> getToDos() {
        return toDos;
    }

    public void setToDos(Set<ToDo> toDos) {
        this.toDos = toDos;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", chairman=" + (chairman==null?"null":chairman.id)+
                ", roles=" + roles +
                ", isActive=" + isActive +
                '}';
    }
}
