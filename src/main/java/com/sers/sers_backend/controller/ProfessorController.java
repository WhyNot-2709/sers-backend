package com.sers.sers_backend.controller;

import com.sers.sers_backend.dto.*;
import com.sers.sers_backend.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professor")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfessorController {

    @Autowired private ProfessorService professorService;

    @GetMapping("/{facultyId}/courses")
    public List<CourseDTO> getCourses(@PathVariable Integer facultyId) {
        return professorService.getCoursesForProfessor(facultyId);
    }

    @GetMapping("/course/{courseId}/students")
    public List<StudentDTO> getStudents(@PathVariable Integer courseId) {
        return professorService.getStudentsForProfessorCourse(courseId);
    }
}