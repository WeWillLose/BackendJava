package com.Diplom.BackEnd.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreListDTO {
    private Long id;
    private JsonNode data;
    private String name;
}
