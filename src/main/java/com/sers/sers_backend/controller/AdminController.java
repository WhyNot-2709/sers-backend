package com.sers.sers_backend.controller;

import com.sers.sers_backend.dto.StudentDTO;
import com.sers.sers_backend.service.AdminService;
import com.sers.sers_backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000") // Matches your React frontend
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private StudentService studentService;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return adminService.getDashboardStats();
    }

    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/courses/{courseId}/students")
    public List<StudentDTO> getStudentsForCourse(@PathVariable Integer courseId) {
        return adminService.getStudentsForCourse(courseId);
    }

    @PostMapping("/override")
    public Map<String, Object> overrideAllocation(
            @RequestParam Integer studentId,
            @RequestParam Integer courseId) {
        return adminService.overrideAllocation(studentId, courseId);
    }

    // NEW ENDPOINT: Removes a student from a course
    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public Map<String, Object> removeStudentFromCourse(
            @PathVariable Integer courseId,
            @PathVariable Integer studentId) {
        return adminService.removeStudentFromCourse(studentId, courseId);
    }
}