package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
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
import java.util.Map;
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
        return   ResponseEntity.ok(reportService.saveReport(report,user.getId()));
    }
    @GetMapping("docx/{id}")
    public ResponseEntity<?> getReportDocx(@PathVariable Long id, @AuthenticationPrincipal User user){
        try{
        InputStreamResource inputStreamResource = reportService.generateReportDocx(id);
        HttpHeaders headers = new HttpHeaders();

        String format = String.format("attachment; filename=%s.docx", UUID.randomUUID().toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION,format );
        return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource);
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @GetMapping("author/current")
    public ResponseEntity<?> getReportByCurrentUser(@AuthenticationPrincipal User user){
        try{
            List<Report> all = reportService.getAllByAuthorId(user.getId());
            List<ReportDTO> reportDTOS = mapperForReportService.mapToReportDTOWithoutData(all);
            return ResponseEntity.ok().body(reportDTOS);
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @GetMapping("chairman/followersReports/{id}")
    public ResponseEntity<?> getReportByChairmanId(@PathVariable(name = "id") Long chairmanID){
        try{
            Map<String, List<ReportDTO>> followersReports = reportService.getFollowersReports(chairmanID);
            return ResponseEntity.ok().body(followersReports);
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @GetMapping("chairman/followersReports/current")
    public ResponseEntity<?> getReportByChairmanId(@AuthenticationPrincipal User user){
        try{
            Map<String, List<ReportDTO>> followersReports = reportService.getFollowersReports(user.getId());
            return ResponseEntity.ok().body(followersReports);
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

    @GetMapping("author/{id}")
    public ResponseEntity<?> getReportByAuthor(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTOWithoutData(reportService.getAllByAuthorId(id)));
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTO(reportService.getByReportId(id)));
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(@PathVariable Long id,@RequestBody ReportDTO reportDTO){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTO(reportService.updateReport(id,reportDTO)));
        }catch (NullPointerExceptionImpl e){
            return new ServerExceptionImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
}
