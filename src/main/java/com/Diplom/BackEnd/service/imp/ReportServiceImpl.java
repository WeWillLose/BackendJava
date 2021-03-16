package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ReportNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.ReportTableRepo;
import com.fasterxml.jackson.databind.JsonNode;
import javassist.bytecode.ByteArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ReportServiceImpl {
    @Autowired
    private ReportTableRepo reportTableRepo;
    @Autowired
    UserServiceImpl userService;

    private final String PATTERN = "\\{\\{([a-zA-z0-9]+)}}";
    @Value("${report.pathToReportTemplate}")
    private String pathToReportTemplate;

    public String validateReportData(JsonNode data) {
        return null;

    }

    private void parsDocx(XWPFDocument docx, String pattern, JsonNode data) {
        insertValuesInTables(docx, pattern, data);
        replacePlaceholders(docx,pattern,data);
    }

    private void replacePlaceholders(XWPFDocument docx, String pattern, JsonNode data) {
        if (docx == null) {
            throw new NullPointerExceptionImpl("IN replacePlaceholders docx is null");
        }
        if (pattern == null || pattern.isBlank()) {
            throw new NullPointerExceptionImpl("IN replacePlaceholders pattern is blank or null");
        }
        if (data == null) {
            throw new NullPointerExceptionImpl("IN replacePlaceholders data is null");
        }
        Pattern regexp = Pattern.compile(pattern);
        for (XWPFParagraph p : docx.getParagraphs()) {

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
                        text = sb.toString().replace(group0, value.toString().replace("\"", ""));
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

    private void insertValuesInTables(XWPFDocument docx, String pattern, JsonNode data) {
        if (docx == null) {
            throw new NullPointerExceptionImpl("IN insertValuesInTables docx is null");
        }
        if (data == null) {
            throw new NullPointerExceptionImpl("IN insertValuesInTables data is null");
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
                                    row.getCell(cellInd).setText(value.toString().replace("\"", ""));
                                } else {
                                    row.getCell(cellInd).setText("");
                                }
                            } else {
                                row.getCell(cellInd).setText("");
                            }
                        }
                    }

                }
                table.removeRow(0);
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
    private InputStreamResource getInputstream(XWPFDocument docx) throws IOException {
        if(docx == null){
            throw new NullPointerExceptionImpl("IN getInputstream docx is null");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        docx.write(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }
    public InputStreamResource generateReportDocx(Long id) {
        Report report = reportTableRepo.findById(id).orElseThrow(ReportNotFoundExceptionImpl::new);

        try (FileInputStream fos = new FileInputStream(pathToReportTemplate)) {
            XWPFDocument docx = new XWPFDocument(fos);
            parsDocx(docx, PATTERN, report.getData());
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

    public Report getReport(Long id) {
        return reportTableRepo.findById(id).orElse(null);
    }

    public Report saveReport(Report report, User author) {
        report.setAuthor(userService.findById(author.getId()));
        return reportTableRepo.save(report);
    }

    public List<Report> getAll() {
        return reportTableRepo.findAll();
    }


}
