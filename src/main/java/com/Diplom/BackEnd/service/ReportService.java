package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.io.InputStreamResource;

import java.util.List;
import java.util.Map;

public interface ReportService {

     InputStreamResource generateReportDocx(Long id);

     Report saveReport(Report report, Long authorId);

     List<Report> getAll();

     List<Report> getAllByAuthorId(Long authorId);

     Report getByReportId(Long reportId); 

     boolean existsById(Long reportId);

     List<Report> getFollowersReports(Long chairmanId);

    Report updateReport(Long id, ReportDTO reportDTO);
    void deleteReport(Long id);
}
