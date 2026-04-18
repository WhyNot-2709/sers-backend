package com.sers.sers_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String role;
    private Integer userId;
    private String name;
    private String message;
}