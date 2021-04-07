package com.Diplom.BackEnd;

import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ValidationExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.ChairmanService;
import com.Diplom.BackEnd.service.UserValidationService;
import com.Diplom.BackEnd.service.imp.CanEditServiceImpl;
import com.Diplom.BackEnd.service.imp.ChairmanServiceImpl;
import com.Diplom.BackEnd.service.imp.UserServiceImpl;
import com.Diplom.BackEnd.service.imp.UserValidationValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepo userRepo;
    @Mock
    RoleRepo roleRepo;
    @Mock
    CanEditServiceImpl canEditService;



    @Spy
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Spy
    UserValidationService userValidationService = new UserValidationValidationServiceImpl();

    SecurityContext securityContext;
    @Spy
    @InjectMocks
    UserServiceImpl userService;

    @Spy
    @InjectMocks
    ChairmanServiceImpl chairmanService;

    User admin = new User() {{
        setId(999L);
        setFirstName("Admin");
        setPatronymic("Admin");
        setLastName("Admin");
        setRoles(Set.of(new Role(ERole.ROLE_ADMIN)));
    }};
    User chairman = new User() {{
        setId(998L);
        setFirstName("Chairman");
        setPatronymic("Chairman");
        setLastName("Chairman");
        setRoles(Set.of(new Role(ERole.ROLE_CHAIRMAN)));
    }};
    User teacher = new User() {{
        setId(997L);
        setFirstName("Teacher");
        setPatronymic("Teacher");
        setLastName("Teacher");
        setRoles(Set.of(new Role(ERole.ROLE_TEACHER)));
    }};
    User all = new User() {{
        setId(996L);
        setFirstName("All");
        setPatronymic("All");
        setLastName("All");
        setRoles(Set.of(new Role(ERole.ROLE_TEACHER), new Role(ERole.ROLE_CHAIRMAN), new Role(ERole.ROLE_ADMIN)));
    }};

    List<Role> roles = new ArrayList() {{
        add(new Role() {{
            setId(1L);
            setName(ERole.ROLE_TEACHER);
        }});
        add(new Role() {{
            setId(2L);
            setName(ERole.ROLE_CHAIRMAN);
        }});
        add(new Role() {{
            setId(3L);
            setName(ERole.ROLE_ADMIN);
        }});
    }};


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(roleRepo.findAll()).thenReturn(roles);
        when(roleRepo.findByName(ERole.ROLE_TEACHER)).thenReturn(roles.get(0));
        when(roleRepo.findByName(ERole.ROLE_CHAIRMAN)).thenReturn(roles.get(1));
        when(roleRepo.findByName(ERole.ROLE_ADMIN)).thenReturn(roles.get(2));

        when(userRepo.save(any())).thenAnswer(t -> t.getArgument(0));

    }

    @Test
    public void when_findById_should_return_user() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN), new Role(ERole.ROLE_TEACHER)));
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        User byId = userService.findById(1L);
        verify(userRepo, times(1)).findById(1L);
        assertThat(byId).isEqualTo(user1);
    }

    @Test()
    public void when_findById_should_throw_return_null() {
        when(userRepo.findById(any())).thenReturn(java.util.Optional.empty());
        User byId = userService.findById(1L);
        assertThat(byId).isEqualTo(null);
        verify(userRepo, times(1)).findById(1L);
    }

    @Test
    public void when_delete_should_delete_user() {
        List<User> db = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_TEACHER)));
        db.add(user1);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        when(canEditService.canEditOnlyAdmin(any())).thenReturn(true);
        userService.delete(1L);
        verify(userRepo, times(1)).delete(any());
    }

    @Test
    public void when_delete_should_throw_ValidationException_cause_RoleAdmin() {
        List<User> db = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN), new Role(ERole.ROLE_TEACHER)));
        db.add(user1);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        assertThatThrownBy(() -> userService.delete(1L)).isInstanceOf(ValidationExceptionImpl.class).hasMessageContaining("Админа нельзя удалить");
        verify(userRepo, never()).delete(any());
    }

    @Test
    public void when_delete_should_throw_NotFoundException() {
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> userService.delete(1L)).isInstanceOf(UserNotFoundExceptionImpl.class).hasMessageContaining("id: 1");
        verify(userRepo, never()).delete(any());
    }

    @Test
    public void when_edit_should_edit_user() {
        User user1 = new User();
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN), new Role(ERole.ROLE_TEACHER)));
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));

        when(canEditService.canEdit(any(), any())).thenReturn(true);
        User editedFirstName = new User() {{
            setFirstName("EditedFirstName");
        }};
        User user2 = userService.updateUserInfo(1L, editedFirstName);
        assertThat(user2)
                .isEqualToIgnoringGivenFields(user1, "firstName")
                .isEqualToComparingOnlyGivenFields(editedFirstName, "firstName");
        verify(userRepo, times(1)).save(any());
    }

    @Test
    public void when_update_userInfo_should_update() {
        User user = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(new Role(ERole.ROLE_CHAIRMAN)));
            setId(1L);
        }};
        User assertUser = new User() {{
            setFirstName("firstName1");
            setLastName("lastNAme2");
            setPatronymic("patronymic3");
            setRoles(Set.of(new Role(ERole.ROLE_CHAIRMAN)));
            setId(1L);
        }};

        User changedUser = new User() {{
            setFirstName("firstName1");
            setLastName("lastNAme2");
            setPatronymic("patronymic3");
            setRoles(Set.of(new Role(ERole.ROLE_TEACHER)));
            setId(2L);
        }};
        when(canEditService.canEdit(any(), any())).thenReturn(true);
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(admin);

        User user1 = userService.updateUserInfo(1L, changedUser);
        assertThat(user1).isEqualTo(assertUser);
        verify(userRepo, times(1)).save(any());
    }

    @Test
    public void when_setRoles_should_setRoles() {
        User user = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(new Role(ERole.ROLE_CHAIRMAN)));
            setId(1L);
        }};
        User assertUser = new User() {{
            setFirstName("firstName1");
            setLastName("lastNAme2");
            setPatronymic("patronymic3");
            setRoles(Set.of(new Role(ERole.ROLE_TEACHER)));
            setId(1L);
        }};
        List<RoleDTO> roleDTOS = new ArrayList<>() {{
            add(new RoleDTO(ERole.ROLE_TEACHER));
        }};
        when(canEditService.canEditOnlyAdmin(any())).thenReturn(true);
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(admin);

        User user1 = userService.setRoles(1L, roleDTOS);
        assertThat(user1).isEqualTo(assertUser);
        verify(userRepo, times(1)).save(any());
    }
    @Test
    public void when_setChairman_should_setChairman() {
        User teacher = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(0)));
            setId(1L);
        }};
        User chairman = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(1)));
            setId(2L);
        }};

        User assertTeacher = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(0)));
            setId(1L);
            setChairman(chairman);
        }};

        User  assertChairman = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(1)));
            setId(2L);
        }};

        when(canEditService.canEditOnlyAdmin(any())).thenReturn(true);
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(chairman));
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(teacher));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(admin);

        User user1 = userService.setChairman(1L, 2L);
        assertThat(user1).isEqualToComparingFieldByField(assertTeacher);
        assertThat(chairman).isEqualToComparingFieldByField(assertChairman);
        verify(userRepo, times(1)).save(any());
    }
    @Test
    public void when_setChairman_should_ThrowValidationError() {
        User teacher = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(0)));
            setId(1L);
        }};
        User chairman = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(0)));
            setId(2L);
        }};

        User assertTeacher = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(0)));
            setId(1L);
        }};

        User  assertChairman = new User() {{
            setFirstName("firstName");
            setLastName("lastNAme");
            setPatronymic("patronymic");
            setRoles(Set.of(roles.get(0)));
            setId(2L);
        }};

        when(canEditService.canEditOnlyAdmin(any())).thenReturn(true);
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.of(chairman));
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(teacher));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(admin);

        assertThatThrownBy(()->userService.setChairman(1L, 2L)).isInstanceOf(ValidationExceptionImpl.class).hasMessage("У пользователя нет роли председатель");
        assertThat(teacher).isEqualToComparingFieldByField(assertTeacher);
        assertThat(chairman).isEqualToComparingFieldByField(assertChairman);
        verify(userRepo, never()).save(any());
    }

}
