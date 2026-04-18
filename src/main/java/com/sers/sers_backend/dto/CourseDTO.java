package com.sers.sers_backend.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Integer id;
    private String code;
    private String name;
    private String type;
    private String streamCode;
    private Integer credits;
    private String days;
    private String startTime;
    private String endTime;
    private String classroom;
    private String facultyName;
    private Integer facultyId;
    private Integer maxSeats;
    private Integer currentSeats;
    private Boolean isActive;
    private String description;
    private Boolean isFixed;
    private String status; // for selections: PREVIEW, CONFIRMED, null
}