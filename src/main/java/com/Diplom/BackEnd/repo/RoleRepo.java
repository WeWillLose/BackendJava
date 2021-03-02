package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(ERole name);
}
