package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.model.EReportStatus;
import com.Diplom.BackEnd.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;

    private String name;

    private JsonNode data;

    private UserDTO author;

    private EReportStatus status;

    private Date createdDate;

    private Date lastModifiedDate;

}
