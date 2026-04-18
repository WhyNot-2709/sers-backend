package com.sers.sers_backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StudentDTO {
    private Integer id;
    private String rollNumber;
    private String name;
    private Integer year;
    private Integer semester;
    private String stream;
    private BigDecimal cgpa;
    private Integer maxCoreElectives;
    private Integer maxHumElectives;
}