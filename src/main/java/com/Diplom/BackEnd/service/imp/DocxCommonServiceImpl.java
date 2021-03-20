package com.Diplom.BackEnd.service.imp;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocxCommonServiceImpl {
    public void removeAllRuns(XWPFParagraph p, int numberOfRuns) {
        if (p == null) {
            return;
        }
        for (int i = 0; i < numberOfRuns; i++) {
            p.removeRun(0);
        }
    }

    public StringBuilder getTextFromAllRuns(XWPFParagraph p) {
        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : p.getRuns()) {
            int pos = r.getTextPosition();
            if (r.getText(pos) != null) {
                sb.append(r.getText(pos));
            }
        }
        return sb;
    }
}
