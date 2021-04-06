package com.Diplom.BackEnd;

import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ValidationExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.UserValidationService;
import com.Diplom.BackEnd.service.imp.CanEditServiceImpl;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepo userRepo ;

    @Mock
    CanEditServiceImpl canEditService;

    @Spy
    UserValidationService userValidationService = new UserValidationValidationServiceImpl();

    @InjectMocks
    UserServiceImpl userService ;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void when_findById_should_return_user(){
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN),new Role(ERole.ROLE_TEACHER)));
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        User byId = userService.findById(1L);
        verify(userRepo,times(1)).findById(1L);
        assertThat(byId).isEqualTo(user1);
    }
    @Test()
    public void when_findById_should_throw_return_null(){
        when(userRepo.findById(any())).thenReturn(java.util.Optional.empty());
        User byId = userService.findById(1L);
        assertThat(byId).isEqualTo(null);
        verify(userRepo,times(1)).findById(1L);
    }

    @Test
    public void when_delete_should_delete_user(){
        List<User> db = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN),new Role(ERole.ROLE_TEACHER)));
        db.add(user1);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        when(canEditService.canEdit(any(),any())).thenReturn(true);

        userService.delete(1L);
        verify(userRepo,times(1)).delete(any());
    }

    @Test
    public void when_delete_should_throw_ValidationException_cause_RoleAdmin(){
        List<User> db = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN),new Role(ERole.ROLE_TEACHER)));
        db.add(user1);

        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        when(canEditService.canEdit(any(),any())).thenReturn(true);
        assertThatThrownBy(()->userService.delete(1L)).isInstanceOf(ValidationExceptionImpl.class).hasMessageContaining("Админа нельзя удалить");
        verify(userRepo,never()).delete(any());
    }

    @Test
    public void when_delete_should_throw_NotFoundException(){
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.empty());
        when(canEditService.canEdit(any(),any())).thenReturn(true);
        assertThatThrownBy(()->userService.delete(1L)).isInstanceOf(UserNotFoundExceptionImpl.class).hasMessageContaining("id: 1");
        verify(userRepo,never()).delete(any());
    }

    @Test
    public void when_edit_should_edit_user(){
        User user1 = new User();
        user1.setPassword("PasswordUser1");
        user1.setUsername("UsernameUser1");
        user1.setPatronymic("PatronymicUser1");
        user1.setFirstName("FirstNameUser1");
        user1.setLastName("LastNameUser1");
        user1.setId(1L);
        user1.setRoles(Set.of(new Role(ERole.ROLE_ADMIN),new Role(ERole.ROLE_TEACHER)));
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user1));
        when(userRepo.save(any())).thenAnswer(t->t.getArgument(0));
        when(canEditService.canEdit(any(),any())).thenReturn(true);
        User editedFirstName = new User() {{
            setFirstName("EditedFirstName");
        }};
        User user2 = userService.updateUserInfo(1L,editedFirstName);
        assertThat(user2)
                .isEqualToIgnoringGivenFields(user1,"firstName")
                .isEqualToComparingOnlyGivenFields(editedFirstName,"firstName");
        verify(userRepo,times(1)).save(any());
    }

}
