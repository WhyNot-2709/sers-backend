package com.sers.sers_backend.dto;

import lombok.Data;

@Data
public class SelectionRequest {
    private Integer studentId;
    private Integer courseId;
    private String action; // PREVIEW, CONFIRM, REMOVE
}