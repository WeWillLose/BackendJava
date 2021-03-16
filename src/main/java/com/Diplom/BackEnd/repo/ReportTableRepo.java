package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportTableRepo extends JpaRepository<Report,Long> {
    List<Report> findAllByAuthorId(Long id);
}
