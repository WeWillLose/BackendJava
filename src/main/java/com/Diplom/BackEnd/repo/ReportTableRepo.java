package com.Diplom.BackEnd.repo;

import com.Diplom.BackEnd.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportTableRepo extends JpaRepository<Report,Long> {
}
