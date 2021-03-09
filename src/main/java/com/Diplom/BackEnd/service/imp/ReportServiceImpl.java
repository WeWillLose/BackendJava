package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ReportTableRepo;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
@Service
public class ReportServiceImpl {
    @Autowired
    private ReportTableRepo reportTableRepo;
    @Autowired
    UserServiceImpl userService;

    public String validateReportData(JsonNode data){
        if(data.isEmpty()){
            return "Таблица пустая";
        }
        if(!data.isArray()){
            return "Таблица должны быть массивом";
        }
        return null;

    }

    public Report getReport(Long id){
       return reportTableRepo.findById(id).orElse(null);
    }

    public Report saveReport(Report report, User author){
        report.setAuthor(userService.findById(author.getId()));
        System.out.println(author);
        return reportTableRepo.save(report);
    }

    public List<Report> getAll() {
        return reportTableRepo.findAll();
    }


}
