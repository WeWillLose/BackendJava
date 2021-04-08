package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.InputStreamResourceDTO;
import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.ServerExceptionImpl;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.ReportService;
import com.Diplom.BackEnd.service.imp.ReportMapperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportMapperServiceImpl mapperForReportService;

    @PostMapping("save")
    public ResponseEntity<?> saveReport(@RequestBody Report report, @AuthenticationPrincipal User user){
        try{
            return   ResponseEntity.ok(reportService.saveReport(report,user.getId()));
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }

    }
    @GetMapping("docx/{id}")
    public ResponseEntity<?> getReportDocx(@PathVariable Long id){
        try{
        InputStreamResourceDTO inputStreamResource = reportService.generateReportDocx(id);
        HttpHeaders headers = new HttpHeaders();
        String format = String.format("attachment; filename=%s", inputStreamResource.getFileName());
        headers.add(HttpHeaders.CONTENT_DISPOSITION,format );
        return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource.inputStreamResource);
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @GetMapping("author/current")
    public ResponseEntity<?> getReportByCurrentUser(@AuthenticationPrincipal User user){
        try{
            List<ReportDTO> reportDTOS = mapperForReportService.mapToReportDTOWithoutData(reportService.getAllByAuthorId(user.getId()));
            return ResponseEntity.ok().body(reportDTOS);
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @GetMapping("followersReports/{id}")
    public ResponseEntity<?> getReportByChairmanId(@PathVariable(name = "id") Long chairmanID){
        try{
            List<ReportDTO> followersReports = mapperForReportService.mapToReportDTOWithoutData(reportService.getFollowersReports(chairmanID));
            return ResponseEntity.ok().body(followersReports);
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @GetMapping("followersReports/current")
    public ResponseEntity<?> getReportByChairmanId(@AuthenticationPrincipal User user){
        try{
            List<ReportDTO> followersReports = mapperForReportService.mapToReportDTOWithoutData(reportService.getFollowersReports(user.getId()));
            return ResponseEntity.ok().body(followersReports);
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteReportById(@PathVariable Long id){
        try{
            reportService.deleteReport(id);
            return ResponseEntity.ok().body(null);
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @GetMapping("author/{id}")
    public ResponseEntity<?> getReportByAuthor(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTOWithoutData(reportService.getAllByAuthorId(id)));
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTO(reportService.getByReportId(id)));
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(@PathVariable Long id,@RequestBody ReportDTO reportDTO){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTO(reportService.updateReport(id,reportDTO)));
        } catch (MyException e) {
            return e.getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
}
