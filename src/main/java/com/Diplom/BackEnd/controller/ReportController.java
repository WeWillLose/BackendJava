package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.imp.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/report")
public class ReportController {
    @Autowired
    private ReportServiceImpl reportService;

    @GetMapping("{id}")
    public ResponseEntity getReport(@PathVariable(name = "id") Report report){
        return   ResponseEntity.ok(report);
    }

    @GetMapping("")
    public ResponseEntity getReport(){
        return   ResponseEntity.ok(reportService.getAll());
    }

    @PostMapping("")
    public ResponseEntity createReport(@RequestBody Report report, @AuthenticationPrincipal User user){
        return   ResponseEntity.ok(reportService.saveReport(report,user));
    }
}
