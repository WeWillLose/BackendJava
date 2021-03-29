package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.model.Report;

import java.util.List;
import java.util.stream.Collectors;

public interface ReportMapperService {
    List<ReportDTO> mapToReportDTO(List<Report> reports);

    ReportDTO mapToReportDTO(Report report);

    List<ReportDTO> mapToReportDTOWithoutData(List<Report> reports);

    ReportDTO mapToReportDTOWithoutData(Report report);
}
