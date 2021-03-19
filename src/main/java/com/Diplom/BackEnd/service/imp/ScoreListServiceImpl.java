package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.ScoreListDTO;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.service.ReportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ScoreListServiceImpl {
    @Autowired
    private ReportService reportService;

    public ScoreListDTO getScoreList(Long reportId) throws IOException {
        if (reportId == null) {
            throw new NullPointerExceptionImpl("id is null");
        }
        if (reportService.existsById(reportId)) {
            Report byReportId = reportService.getByReportId(reportId);
            generateScoreList(byReportId);
        }
        return null;
    }

    private Double getScoreSum(JsonNode dataRows){
        double score = 0;
        for (JsonNode datum : dataRows) {
            if(datum.get("score")!=null){
                try{
                    score+=datum.get("score").asDouble();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        System.out.println(score);
        return score;
    }

    private ScoreListDTO generateScoreList(Report report) throws IOException {
        JsonNode reportData = report.getData();
        XWPFDocument docx = new XWPFDocument(new BufferedInputStream(new FileInputStream("src\\main\\resources\\docx_templates\\template_score_list.docx")));
        List<XWPFTable> tables = docx.getTables();
        if (tables == null || tables.isEmpty()) {
            return null;
        }
        ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
        jsonNodes.put("1", getScoreSum(reportData.get("comment")));
        jsonNodes.put("1.1",getScoreSum(reportData.get("comment")));
        jsonNodes.put("2.1",getScoreSum(reportData.get("creation")));
        jsonNodes.put("2.2",getScoreSum(reportData.get("proective")));
        jsonNodes.get("");
        return null;
    }

}
