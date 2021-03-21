package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.service.ReportMapperService;
import com.Diplom.BackEnd.service.UserMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportMapperServiceImpl implements ReportMapperService {
    @Autowired
    UserMapperService userMapperService;
    public List<ReportDTO> mapToReportDTO(List<Report> reports) {
        if(reports == null){
            return null;
        }
        return reports.stream().map(this::mapToReportDTO).collect(Collectors.toList());
    }
    public ReportDTO mapToReportDTO(Report report) {
        ReportDTO reportDTO = new ReportDTO();
        if(report == null){
            return reportDTO;
        }
        if(report.getAuthor()!=null){
            reportDTO.setAuthor(userMapperService.mapToUserDto(report.getAuthor()));
        }

        reportDTO.setData(report.getData());
        reportDTO.setName(report.getName());
        reportDTO.setId(report.getId());
        reportDTO.setStatus(report.getStatus());
        reportDTO.setCreatedDate(report.getCreatedDate());
        reportDTO.setLastModifiedDate(report.getLastModifiedDate());
        return reportDTO;
    }
    public List<ReportDTO> mapToReportDTOWithoutData(List<Report> reports) {
        if(reports == null){
            return null;
        }
        return reports.stream().map(this::mapToReportDTO).collect(Collectors.toList());
    }

    public ReportDTO mapToReportDTOWithoutData(Report report) {
        ReportDTO reportDTO = new ReportDTO();
        if(report == null){
            return reportDTO;
        }
        if(report.getAuthor()!=null){
            reportDTO.setAuthor(userMapperService.mapToUserDto(report.getAuthor()));
        }
        reportDTO.setName(report.getName());
        reportDTO.setId(report.getId());
        reportDTO.setStatus(report.getStatus());
        reportDTO.setCreatedDate(report.getCreatedDate());
        reportDTO.setLastModifiedDate(report.getLastModifiedDate());
        return reportDTO;
    }

    @Override
    public ReportDTO mapToReport(ReportDTO report) {
        return null;
    }
}
