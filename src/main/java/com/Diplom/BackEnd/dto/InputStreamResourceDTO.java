package com.Diplom.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputStreamResourceDTO {
    public InputStreamResource inputStreamResource;
    public String fileName;

}
