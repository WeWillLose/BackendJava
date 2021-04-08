package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.model.Report;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileNameServiceImpl {
   public String getReportFileNameOrDefault(String name){
        if(name == null || name.isBlank()){
            return String.format("report_%s.docx", UUID.randomUUID().toString());
        }else{
            String res = name.replaceAll("&&&.*", "");
            return String.format("report_%s.docx",res);
        }
    }
    public String getScoreListFileNameOrDefault(String name){
        if(name == null || name.isBlank()){
            return String.format("scoreList_%s.docx", UUID.randomUUID().toString());
        }else{
            String res = name.replaceAll("&&&.*", "");
            return String.format("scoreList_%s.docx",res);
        }
    }
    public String AddSuffixToNameOrDefault(String name){
       if(name ==null || name.isBlank()){
           return String.format("&&&%s",UUID.randomUUID().toString());
       }
       return String.format("%s&&&%s",name,UUID.randomUUID().toString());
    }
}
