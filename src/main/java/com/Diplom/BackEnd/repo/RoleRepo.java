package com.Diplom.BackEnd.repo;
import java.util.Optional;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
