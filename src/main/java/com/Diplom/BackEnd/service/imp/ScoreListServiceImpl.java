package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ReportNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.service.ReportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ScoreListServiceImpl {
    @Autowired
    private ReportService reportService;
    @Value("${report.pathToScoreListTemplate}")
    private String pathToScoreListTemplate;

    private final String REGEXP = "\\{\\{(.+?)}}";

    private void replacePlaceholders(List<XWPFParagraph> paragraphs, String pattern, JsonNode data) {
        if (paragraphs == null) {
            throw new NullPointerExceptionImpl("IN replacePlaceholders paragraphs is null");
        }
        if (pattern == null || pattern.isBlank()) {
            throw new NullPointerExceptionImpl("IN replacePlaceholders pattern is blank or null");
        }
        if (data == null) {
            throw new NullPointerExceptionImpl("IN replacePlaceholders data is null");
        }
        Pattern regexp = Pattern.compile(pattern);
        for (XWPFParagraph p : paragraphs) {

            int numberOfRuns = p.getRuns().size();

            // Collate text of all runs
            StringBuffer sb = new StringBuffer();
            for (XWPFRun r : p.getRuns()) {
                int pos = r.getTextPosition();
                if (r.getText(pos) != null) {
                    sb.append(r.getText(pos));
                }
            }
            // Continue if there is text
            if (sb.length() > 0) {
                Matcher matcher = regexp.matcher(sb.toString());
                while (matcher.find()) {
                    // Remove all existing runs
                    for (int i = 0; i < numberOfRuns; i++) {
                        p.removeRun(0);
                    }
                    String group0 = matcher.group(0);
                    String group1 = matcher.group(1);
                    JsonNode value = data.get(group1);
                    String text = "";
                    if (value != null) {
                        text = sb.toString().replace(group0, value.asText());
                        log.info("IN replacePlaceholders. {} replaced: {}",group0,value);
                    } else {
                        log.warn("IN replacePlaceholders {} not found in data", group1);
                        text = sb.toString().replace(group0, "");
                        log.info("IN replacePlaceholders. {} replaced: ''",group0);
                    }

                    sb.delete(0,sb.length());
                    sb.append(text);
                    // Add new run with updated text
                    XWPFRun run = p.createRun();
                    run.setText(text);
                }
            }
        }
    }
    private Map<Integer, String> parsCells(XWPFTableRow row, String pattern) {
        Map<Integer, String> res = new HashMap<>();
        if (row == null) {
            return res;
        }
        for (int i = 0; i < row.getTableCells().size(); i++) {
            String filedName = parsCell(row.getCell(i), pattern);
            if (filedName != null) {
                res.put(i, filedName);
            }
        }
        return res;
    }
    private void deletePlaceholders(XWPFTableCell xwpfTableCell) {
        if(xwpfTableCell==null){
            return;
        }
        for (XWPFParagraph paragraph : xwpfTableCell.getParagraphs()) {
            for (XWPFRun r : paragraph.getRuns()) {
                String color = r.getColor();
                if (color != null) {
                    if (color.equals("7030A0")) {
                        r.setText("", 0);
                    }
                }
            }
        }
    }
    private String parsCell(XWPFTableCell xwpfTableCell, String pattern) {
        if (xwpfTableCell == null || pattern == null) {
            return null;
        }
        Pattern regexp = Pattern.compile(pattern);
        Matcher matcher = regexp.matcher(xwpfTableCell.getText());
        if (matcher.find()) {
//            String replace = matcher.group(0);
            String fieldName = matcher.group(1);
            deletePlaceholders(xwpfTableCell);
            return fieldName;
        }
        return null;
    }
    public void replacePlaceholdersInTables(XWPFDocument docx, String pattern, JsonNode data, boolean delete0Row) {
        if (docx == null) {
            throw new NullPointerExceptionImpl("IN replacePlaceholdersInTables docx is null");
        }
        if (data == null) {
            throw new NullPointerExceptionImpl("IN replacePlaceholdersInTables data is null");
        }
        List<XWPFTable> tables = docx.getTables();
        if (tables == null || tables.isEmpty()) {
            return;
        }
        Pattern regexp = Pattern.compile(pattern);
        for (XWPFTable table : tables) {
            if (table.getRows().size() != 2) {
                continue;
            }
            Matcher matcher = regexp.matcher(table.getRow(0).getCell(0).getText());
            if (matcher.find()) {
                String tableName = matcher.group(1);
                Map<Integer, String> colField = parsCells(table.getRow(1), pattern);
                JsonNode rowsData = data.get(tableName);
                if (rowsData != null) {
                    if (!rowsData.isArray()) {
                        throw new BadRequestImpl();
                    }
                    for (JsonNode rowData : rowsData) {
                        XWPFTableRow row = table.createRow();
                        for (int cellInd = 0; cellInd < row.getTableCells().size(); cellInd++) {
                            if (colField.containsKey(cellInd)) {
                                JsonNode value = rowData.get(colField.get(cellInd));
                                if (value != null) {
                                    row.getCell(cellInd).setText(value.asText());
                                } else {
                                    row.getCell(cellInd).setText("");
                                }
                            } else {
                                row.getCell(cellInd).setText("");
                            }
                        }
                    }

                }
                if(delete0Row){
                    table.removeRow(0);
                }

            }
        }
    }
    private Double getScoreSum(JsonNode dataRows) {
        double score = 0;
        if(dataRows == null){
            return score;
        }
        for (JsonNode datum : dataRows) {
            if (datum.get("score") != null) {
                try {
                    score += datum.get("score").asDouble();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return score;
    }

    private Double getScoreSum(JsonNode dataRows,int max) {
        double score = 0;
        if(dataRows == null){
            return score;
        }
        for (JsonNode datum : dataRows) {
            if (datum.get("score") != null) {
                try {
                    score += datum.get("score").asDouble();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return score>max?max:score;
    }
    private Double getScoreSum(JsonNode dataRows,int rowInd,Integer max) {
        if(dataRows == null){
            return 0D;
        }
        if (dataRows.get(rowInd) != null && dataRows.get(rowInd).get("score") !=null) {
            try {
                double score = dataRows.get(rowInd).get("score").asDouble(0);
                if(max!=null)
                    return score >max?max:score;
                else return score;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0D;
    }
    private double getProcent1(double score){
        if(score>=11 && score<=15){
            return 15;
        }
        if(score>=6 && score<=10){
            return 10;
        }
        if(score>=4 && score<=5){
            return 5;
        }
        return 0;
    }


    private double getProcent2(double score){
        if(score>40){
            return 30;
        }
        if(score>=31 && score<=40){
            return 20;
        }
        if(score>=21 && score<=30){
            return 15;
        }
        if(score>=11 && score<=20){
            return 10;
        }
        if(score<=10){
            return 5;
        }
        return -1;
    }
    private InputStreamResource getInputstream(XWPFDocument docx) throws IOException {
        if(docx == null){
            throw new NullPointerExceptionImpl("IN getInputstream docx is null");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        docx.write(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }
    private void parsScoreListTemplate(XWPFDocument docx, String REGEXP, JsonNode reportData){
        List<XWPFTable> tables = docx.getTables();
        if (tables == null || tables.isEmpty()) {
            return;
        }
        ObjectNode data = getData(reportData);
        Pattern regexp = Pattern.compile(REGEXP);
        for (XWPFTable table : docx.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell tableCell : row.getTableCells()) {
                    for (XWPFParagraph p : tableCell.getParagraphs()) {
                        int numberOfRuns = p.getRuns().size();
                        // Collate text of all runs
                        StringBuffer sb = new StringBuffer();
                        for (XWPFRun r : p.getRuns()) {
                            int pos = r.getTextPosition();
                            if (r.getText(pos) != null) {
                                sb.append(r.getText(pos));
                            }
                        }
                        // Continue if there is text and contains "test"
                        if (sb.length() > 0) {
                            Matcher matcher = regexp.matcher(sb.toString());
                            while (matcher.find()) {
                                // Remove all existing runs
                                for (int i = 0; i < numberOfRuns; i++) {
                                    p.removeRun(0);
                                }
                                String group0 = matcher.group(0);
                                String group1 = matcher.group(1);
                                JsonNode value = data.get(group1);
                                String text = "";
                                if (value != null) {
                                    text = sb.toString().replace(group0, value.asText());
                                } else {
                                    log.warn("IN replacePlaceholders {} not found in data", group1);
                                    text = sb.toString().replace(group0, "");
                                }
                                sb.delete(0,sb.length());
                                sb.append(text);
                                // Add new run with updated text
                                XWPFRun run = p.createRun();
                                run.setText(text);
                            }
                        }
                    }
                }
            }
        }
        data.put("fio",reportData.get("fio")!=null?reportData.get("fio").asText():"");
        data.put("fioShort",reportData.get("fioShort")!=null?reportData.get("fioShort").asText():"");
        data.put("quarter",reportData.get("quarter")!=null?reportData.get("quarter").asText():"");
        data.put("year2",reportData.get("year2")!=null?reportData.get("year2").asText():"");
        replacePlaceholders(docx.getParagraphs(),REGEXP,data);
    }
    private String IsSmth(JsonNode data){
        if(data == null || data.isEmpty()){
            return "-";
        }
        if(data.get(0).isEmpty()){
            return "-";
        }
        return "+";
    }

    private ObjectNode getData(JsonNode reportData) {
        ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
        jsonNodes.put("1", getScoreSum(reportData.get("comment"),8));
        jsonNodes.put("1.1", getScoreSum(reportData.get("comment")));
        jsonNodes.put("2.1", getScoreSum(reportData.get("creation")));
        jsonNodes.put("2.2", getScoreSum(reportData.get("proective")));
        jsonNodes.put("2.2.1", getScoreSum(reportData.get("proective"),0,null));
        jsonNodes.put("2.2.2", getScoreSum(reportData.get("proective"),1,null));
        double g2 = jsonNodes.get("2.1").asDouble(0) + jsonNodes.get("2.2").asDouble(0);
        jsonNodes.put("2",g2>8?8:g2);
        jsonNodes.put("sum1", jsonNodes.get("1").asDouble(0)+jsonNodes.get("2").asDouble(0));
        jsonNodes.put("procent1", getProcent1(jsonNodes.get("sum1").asDouble(0)));
        jsonNodes.put("3.1", IsSmth(reportData.get("working_program")));
        jsonNodes.put("3.2.1", getScoreSum(reportData.get("class_rooms")));
        jsonNodes.put("3.2.2", getScoreSum(reportData.get("programs")));
        jsonNodes.put("3.2.3", getScoreSum(reportData.get("reconstruction")));
        jsonNodes.put("3.2", jsonNodes.get("3.2.1").asDouble(0) + jsonNodes.get("3.2.2").asDouble(0) + jsonNodes.get("3.2.3").asDouble(0));
        jsonNodes.put("3.3", getScoreSum(reportData.get("complex")));
        jsonNodes.put("3.4", getScoreSum(reportData.get("teachingaids")));
        jsonNodes.put("3.5", getScoreSum(reportData.get("education")));
        jsonNodes.put("3.6", getScoreSum(reportData.get("sdo")));
        double g3 = jsonNodes.get("3.2").asDouble(0) + jsonNodes.get("3.3").asDouble(0) + jsonNodes.get("3.4").asDouble(0) + jsonNodes.get("3.5").asDouble(0) + jsonNodes.get("3.6").asDouble(0);
        jsonNodes.put("3",g3>13?13:g3 );
        jsonNodes.put("4.1", getScoreSum(reportData.get("plan")));
        jsonNodes.put("4.2", getScoreSum(reportData.get("circle")));
        jsonNodes.put("4.3", getScoreSum(reportData.get("institutions")));
        jsonNodes.put("4.4", getScoreSum(reportData.get("events")));
        double g4 = jsonNodes.get("4.1").asDouble(0) + jsonNodes.get("4.2").asDouble(0) + jsonNodes.get("4.3").asDouble(0) + jsonNodes.get("4.4").asDouble(0);
        jsonNodes.put("4", g4>6?6:g4);
        jsonNodes.put("5.1", IsSmth(reportData.get("plan_group")));
        jsonNodes.put("5.2", getScoreSum(reportData.get("coolhours")));
        jsonNodes.put("5.3", getScoreSum(reportData.get("activity")));
        jsonNodes.put("5.4", getScoreSum(reportData.get("obz")));
        double g5 = jsonNodes.get("5.2").asDouble(0) + jsonNodes.get("5.3").asDouble(0) + jsonNodes.get("5.4").asDouble(0);
        jsonNodes.put("5", g5>4?4:g5);
        jsonNodes.put("6.1.1", getScoreSum(reportData.get("selfeducation")));
        jsonNodes.put("6.1.2", getScoreSum(reportData.get("qualification")));
        jsonNodes.put("6.1.3", getScoreSum(reportData.get("seminars")));
        jsonNodes.put("6.1.4", getScoreSum(reportData.get("participation")));
        jsonNodes.put("6.1", jsonNodes.get("6.1.1").asDouble(0) + jsonNodes.get("6.1.2").asDouble(0) + jsonNodes.get("6.1.3").asDouble(0) + jsonNodes.get("6.1.4").asDouble(0));
        jsonNodes.put("6.2", getScoreSum(reportData.get("contest")));
        double g6 = jsonNodes.get("6.1").asDouble(0) + jsonNodes.get("6.2").asDouble(0);
        jsonNodes.put("6", g6>7?7:g6);
        jsonNodes.put("7", getScoreSum(reportData.get("technologies"),2));
        jsonNodes.put("8", getScoreSum(reportData.get("experience"),4));
        jsonNodes.put("9", getScoreSum(reportData.get("interaction"),4));
        jsonNodes.put("9.1", getScoreSum(reportData.get("interaction"),0,null));
        jsonNodes.put("9.2", getScoreSum(reportData.get("interaction"),1,null));
        jsonNodes.put("9.3", getScoreSum(reportData.get("interaction"),2,null));
        jsonNodes.put("9.4", getScoreSum(reportData.get("interaction"),3,null));
        jsonNodes.put("9.5", getScoreSum(reportData.get("interaction"),4,null));
        jsonNodes.put("10", getScoreSum(reportData.get("subject"),1));
        jsonNodes.put("10.1", getScoreSum(reportData.get("subject"),1));
        jsonNodes.put("11", getScoreSum(reportData.get("manual"),20));
        jsonNodes.put("sum2", jsonNodes.get("3").asDouble(0)+jsonNodes.get("4").asDouble(0)+jsonNodes.get("5").asDouble(0)
                +jsonNodes.get("6").asDouble(0)+jsonNodes.get("7").asDouble(0)+jsonNodes.get("8").asDouble(0)+jsonNodes.get("9").asDouble(0)+jsonNodes.get("10").asDouble(0)
                +jsonNodes.get("11").asDouble(0));
        jsonNodes.put("procent2", getProcent2(jsonNodes.get("sum2").asDouble(0)));
        return jsonNodes;
    }

    private InputStreamResource generateScoreList(Report report,String REGEXP) throws IOException {
        try (FileInputStream fos = new FileInputStream(pathToScoreListTemplate)) {
            XWPFDocument docx = new XWPFDocument(fos);
            parsScoreListTemplate(docx, REGEXP, report.getData());
            return getInputstream(docx);
        } catch (NullPointerExceptionImpl e) {
            e.printStackTrace();
            throw new ServerErrorImpl();
        } catch (MyException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorImpl();
        }
    }
    public InputStreamResource getScoreList(Long reportId) throws IOException {
        if (reportId == null) {
            throw new NullPointerExceptionImpl("id is null");
        }
        Report byReportId = reportService.getByReportId(reportId);
        if (byReportId != null) {
            return generateScoreList(byReportId, REGEXP);
        }else{
            throw new ReportNotFoundExceptionImpl();
        }
    }

}
