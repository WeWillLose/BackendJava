package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.InputStreamResourceDTO;
import com.Diplom.BackEnd.dto.ReportDTO;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.*;
import com.Diplom.BackEnd.model.EReportStatus;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ReportTableRepo;
import com.Diplom.BackEnd.service.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportTableRepo reportTableRepo;

    @Autowired
    UserService userService;

    @Autowired
    ReportDocxServiceImpl reportDocxService;

    @Autowired
    UserMapperService userMapperService;

    @Autowired
    ReportMapperService reportMapperService;

    @Autowired
    CanEditService canEditService;

    private final String PATTERN = "\\{\\{([a-zA-z0-9]+)}}";


    public InputStreamResourceDTO generateReportDocx(Long reportId) {
        if(reportId == null){
            throw new NullPointerExceptionImpl("reportId is null");
        }

        Report report = reportTableRepo.findById(reportId).orElse(null);

        if(report == null){
            throw new ReportNotFoundExceptionImpl(reportId);
        }
        return new InputStreamResourceDTO(reportDocxService.createReportDocx(report,PATTERN),report.getName());
    }

    public Report saveReport(Report report, Long authorId) {
        if(authorId == null){throw new NullPointerExceptionImpl("IN saveReport authorId is null");}
        User author = userService.findById(authorId);
        if(author == null){
            throw new UserNotFoundExceptionImpl(authorId);
        }
        if(report.getName() ==null || report.getName().isBlank()){
            report.setName(generateReportDocxNameFromAuthorFIOOrUUID(author));
        }
        if(report.getData()!=null && author.getChairman() !=null){
            ((ObjectNode)report.getData()).put("chairmanFIO", String.format("%s %.1s. %.1s.",author.getChairman().getLastName(),author.getChairman().getFirstName(),
                    author.getChairman().getPatronymic()));
        }
        if(report.getData()!=null && ((ObjectNode)report.getData()).get("fioShort") == null){
            ((ObjectNode)report.getData()).put("fioShort", String.format("%s %.1s.%.1s.",author.getLastName(),author.getFirstName(),
                    author.getPatronymic()));
        }
        report.setAuthor(author);
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
    @Override
    public List<Report> getFollowersReports(Long chairmanId) {
        if(chairmanId == null){
            throw new NullPointerExceptionImpl("chairmanId is null");
        }
        List<User> followers = userService.findFollowers(chairmanId);

        if(followers == null){
            throw new UserNotFoundExceptionImpl(chairmanId);
        }

        List<Report> reports = new ArrayList<>();

        followers.forEach(t->{
            reports.addAll(getAllByAuthorId(t.getId()));
        });

        return reports;

    }

    @Override
    public Report updateReport(Long id, ReportDTO reportDTO) {
        if(id == null){
            throw new NullPointerExceptionImpl("id is null");
        }
        if(reportDTO == null){
            throw new NullPointerExceptionImpl("reportDTO is null");
        }
        Report report = reportTableRepo.findById(id).orElse(null);
        if(report == null){
            throw new ReportNotFoundExceptionImpl(id);
        }
        if(!canEditService.canEditReportWithChairman((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),report)){
            throw new ForbiddenExceptionImpl();
        }
        if(reportDTO.getName()!=null&& !reportDTO.getName().isBlank()){
            report.setName(reportDTO.getName());
        }
        if(reportDTO.getData()!=null){
            report.setData(reportDTO.getData());
        }
        if(reportDTO.getStatus()!=null){
            report.setStatus(reportDTO.getStatus());
        }

        return reportTableRepo.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        Report report = getByReportId(id);
        if(report == null){
            throw  new ReportNotFoundExceptionImpl(id);
        }
        if(!canEditService.canEditReportWithChairman((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),report)){
            throw new ForbiddenExceptionImpl();
        }
        reportTableRepo.delete(report);
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

    private String generateReportDocxNameFromAuthorFIOOrUUID(User author){
        if(author == null){
            throw new NullPointerExceptionImpl("IN generateReportDocxNameFromAuthorFIOOrUUID author is null");
        }
        String name = userService.getFIO(author);

        if(name == null || name.isBlank()){
            name = UUID.randomUUID().toString();
        }

        return (String.format("report_%s.docx",name.replace(" ","_")));
    }


}
