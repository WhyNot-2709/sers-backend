package com.sers.sers_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username; // roll number for student, email for admin/prof
    private String password;
    private String role;     // STUDENT, ADMIN, PROFESSOR
}