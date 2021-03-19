package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

public interface ReportService {
     String validateReportData(JsonNode data); 
    
     InputStreamResource generateReportDocx(Long id);

     Report saveReport(Report report, User author); 

     List<Report> getAll();

     List<Report> getAllByAuthorId(Long authorId); 
     Report getByReportId(Long reportId); 

     boolean existsById(Long reportId);
}
