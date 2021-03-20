package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.Report;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ReportDocxServiceImpl {
    @Autowired
    DocxCommonServiceImpl docxCommonService;

    @Value("${report.pathToReportTemplate}")
    private String pathToReportTemplate;


    private void parsReportTemplate(XWPFDocument docx, String pattern, JsonNode data) {
        replacePlaceholdersInTables(docx, pattern, data);
        replacePlaceholders(docx.getParagraphs(), pattern, data);
    }

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
        String placeholder;
        String valueInPlaceholder;
        JsonNode value;
        String text;
        for (XWPFParagraph p : paragraphs) {

            int numberOfRuns = p.getRuns().size();

            StringBuilder sb = docxCommonService.getTextFromAllRuns(p);
            if (sb.length() > 0) {
                Matcher matcher = regexp.matcher(sb.toString());
                while (matcher.find()) {
                    docxCommonService.removeAllRuns(p, numberOfRuns);
                    placeholder = matcher.group(0);
                    valueInPlaceholder = matcher.group(1);
                    value = data.get(valueInPlaceholder);
                    if (value != null) {
                        text = sb.toString().replace(placeholder, value.asText());
                    } else {
                        log.warn("IN replacePlaceholders {} not found in data", valueInPlaceholder);
                        text = sb.toString().replace(placeholder, "");
                    }
                    sb.delete(0, sb.length());
                    sb.append(text);
                    // Add new run with updated text
                    XWPFRun run = p.createRun();
                    run.setText(text);
                }
            }
        }
    }



    private void replacePlaceholdersInTables(XWPFDocument docx, String regexp, JsonNode data) {
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
        Pattern pattern = Pattern.compile(regexp);
        for (XWPFTable table : tables) {
            if (table.getRows().size() != 2) {
                continue;
            }
            //tablename
            Matcher matcher = pattern.matcher(table.getRow(0).getCell(0).getText());
            if (matcher.find()) {
                String tableName = matcher.group(1);
                Map<Integer, String> colField = parsCells(table.getRow(1), regexp);
                JsonNode rowsData = data.get(tableName);
                if (rowsData != null) {
                    if (!rowsData.isArray()) {
                        throw new RuntimeException(String.format("IN replacePlaceholdersInTables rowsData is %s, but must be array", rowsData.getNodeType().toString()));
                    }
                    fillTable(table, colField, rowsData);
                }
                table.removeRow(0);
            }
        }

    }

    private void fillTable(XWPFTable table, Map<Integer, String> colField, JsonNode rowsData) {
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

    private Map<Integer, String> parsCells(XWPFTableRow row, String pattern) {
        Map<Integer, String> res = new HashMap<>();
        if (row == null) {
            return res;
        }
        for (int i = 0; i < row.getTableCells().size(); i++) {
            String filedName = parsCellAndDeletePlaceholder(row.getCell(i), pattern);
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

    private String parsCellAndDeletePlaceholder(XWPFTableCell xwpfTableCell, String pattern) {
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

    private InputStreamResource getInputStream(XWPFDocument docx) throws IOException {
        if (docx == null) {
            throw new NullPointerExceptionImpl("IN getInputstream docx is null");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            docx.write(byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        }
    }

    public InputStreamResource createReportDocx(Report report, String REGEXP) {
        try (FileInputStream fos = new FileInputStream(pathToReportTemplate)) {
            XWPFDocument docx = new XWPFDocument(fos);
            parsReportTemplate(docx, REGEXP, report.getData());
            return getInputStream(docx);
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
}
