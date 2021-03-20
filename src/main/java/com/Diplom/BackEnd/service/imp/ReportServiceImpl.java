package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.*;
import com.Diplom.BackEnd.model.EReportStatus;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ReportTableRepo;
import com.Diplom.BackEnd.service.ReportService;
import com.Diplom.BackEnd.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportTableRepo reportTableRepo;
    @Autowired
    UserService userService;
    @Autowired
    ReportDocxServiceImpl reportDocxService;

    private final String PATTERN = "\\{\\{([a-zA-z0-9]+)}}";


    public InputStreamResource generateReportDocx(Long reportId) {
        if(reportId == null){
            throw new NullPointerExceptionImpl("reportId is null");
        }
        Report report = reportTableRepo.findById(reportId).orElse(null);
        if(report == null){
            throw new ReportNotFoundExceptionImpl();
        }
        return reportDocxService.createReportDocx(report,PATTERN);
    }

    public Report saveReport(Report report, User author) {
        if(author.getFirstName()!=null && !author.getFirstName().isBlank() &&
                author.getLastName()!=null && !author.getLastName().isBlank() &&
                author.getPatronymic()!=null && !author.getPatronymic().isBlank()){
            report.setName(String.format("report_%s_%s_%s.docx",author.getLastName(),author.getFirstName(),author.getPatronymic()));
        }else{
            report.setName(String.format("report_%s.docx",UUID.randomUUID().toString()));
        }

        report.setAuthor(userService.findById(author.getId()));
        report.setStatus(EReportStatus.UNCHECKED);
        return reportTableRepo.save(report);
    }

    public List<Report> getAll() {
        return reportTableRepo.findAll();
    }

    public List<Report> getAllByAuthorId(Long authorId) {
        if(authorId == null){
            throw new NullPointerExceptionImpl("authorId is null");
        }
        if(!userService.existsById(authorId)){
            throw new UserNotFoundExceptionImpl();
        }
        return reportTableRepo.findAllByAuthorId(authorId);
    }
    public Report getByReportId(Long reportId) {
        if(reportId == null){
            throw new NullPointerExceptionImpl("reportId is null");
        }
        return reportTableRepo.findById(reportId).orElseThrow(ReportNotFoundExceptionImpl::new);
    }

    public boolean existsById(Long reportId) {
        if(reportId == null){
            log.warn("IN existsById reportId is null");
            return false;
        }
        return reportTableRepo.existsById(reportId);
    }


}
