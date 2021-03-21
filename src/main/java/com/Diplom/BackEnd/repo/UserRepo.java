package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findAllByRolesNotContains(Role role);
    List<User> findAllByRolesContains(Role role);
    List<User> findAllByChairmanId(Long id);
}
