package com.Diplom.BackEnd.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.List;

public interface ReplacePlaceholderService {
    public void replacePlaceholders(List<XWPFParagraph> paragraphs, String pattern, JsonNode data);
    public void replacePlaceholdersInTables(XWPFDocument docx, String pattern, JsonNode data, boolean delete0Row);
}
