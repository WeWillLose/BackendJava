package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.ReportService;
import com.Diplom.BackEnd.service.imp.ReportMapperServiceImpl;
import com.Diplom.BackEnd.service.imp.ReportServiceImpl;
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
    public ResponseEntity saveReport(@RequestBody Report report, @AuthenticationPrincipal User user){
        return   ResponseEntity.ok(reportService.saveReport(report,user));
    }
    @GetMapping("docx/{id}")
    public ResponseEntity<?> getReportDocx(@PathVariable Long id, @AuthenticationPrincipal User user){
        try{
        InputStreamResource inputStreamResource = reportService.generateReportDocx(id);
        HttpHeaders headers = new HttpHeaders();
        String filename = "";
        String format = String.format("attachment; filename=%s.docx", UUID.randomUUID().toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION,format );
        return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @GetMapping("author/current")
    public ResponseEntity<?> getReportByCurrentUser(@AuthenticationPrincipal User user){
        try{
            List<Report> all = reportService.getAllByAuthorId(user.getId());
            List<ReportDTO> reportDTOS = mapperForReportService.mapToReportDTOWithoutData(all);
            System.out.println(reportDTOS);
            return ResponseEntity.ok().body(reportDTOS);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @GetMapping("author/{id}")
    public ResponseEntity<?> getReportByAuthor(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTOWithoutData(reportService.getAllByAuthorId(id)));
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @GetMapping("report/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(mapperForReportService.mapToReportDTO(reportService.getByReportId(id)));
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerErrorImpl().getResponseEntity();
        }
    }
}
