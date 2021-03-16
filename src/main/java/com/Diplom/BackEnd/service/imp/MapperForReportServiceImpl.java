package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.service.MapperToUserDTOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MapperForReportServiceImpl {
    @Autowired
    MapperToUserDTOService mapperToUserDTOService;
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
            reportDTO.setAuthor(mapperToUserDTOService.mapToUserDto(report.getAuthor()));
        }

        reportDTO.setData(report.getData());
        reportDTO.setName(report.getName());
        reportDTO.setId(report.getId());
        reportDTO.setStatus(report.getStatus());
        return reportDTO;
    }
}
