package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
