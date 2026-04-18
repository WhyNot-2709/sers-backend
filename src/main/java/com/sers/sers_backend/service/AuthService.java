package com.sers.sers_backend.service;

import com.sers.sers_backend.dto.LoginRequest;
import com.sers.sers_backend.dto.LoginResponse;
import com.sers.sers_backend.entity.*;
import com.sers.sers_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired private StudentRepository studentRepo;
    @Autowired private AdminRepository adminRepo;
    @Autowired private FacultyRepository facultyRepo;

    public LoginResponse login(LoginRequest request) {
        switch (request.getRole().toUpperCase()) {
            case "STUDENT": {
                Optional<Student> student = studentRepo.findByRollNumber(request.getUsername());
                if (student.isPresent() && student.get().getPassword().equals(request.getPassword())) {
                    return new LoginResponse(true, "STUDENT", student.get().getId(), student.get().getName(), "Login successful");
                }
                return new LoginResponse(false, null, null, null, "Invalid credentials");
            }
            case "ADMIN": {
                Optional<Admin> admin = adminRepo.findByEmail(request.getUsername());
                if (admin.isPresent() && admin.get().getPassword().equals(request.getPassword())) {
                    return new LoginResponse(true, "ADMIN", admin.get().getId(), admin.get().getName(), "Login successful");
                }
                return new LoginResponse(false, null, null, null, "Invalid credentials");
            }
            case "PROFESSOR": {
                Optional<Faculty> faculty = facultyRepo.findByEmail(request.getUsername());
                if (faculty.isPresent() && faculty.get().getPassword().equals(request.getPassword())) {
                    return new LoginResponse(true, "PROFESSOR", faculty.get().getId(), faculty.get().getName(), "Login successful");
                }
                return new LoginResponse(false, null, null, null, "Invalid credentials");
            }
            default:
                return new LoginResponse(false, null, null, null, "Invalid role");
        }
    }
}