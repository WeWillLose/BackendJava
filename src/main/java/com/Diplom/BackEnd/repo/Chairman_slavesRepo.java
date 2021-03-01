package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface Chairman_slavesRepo extends JpaRepository<Chairman_Slaves, Long> {
    Chairman_Slaves findByChairman_Username(String username);
    Chairman_Slaves findBySlavesContains(User slave);

}
