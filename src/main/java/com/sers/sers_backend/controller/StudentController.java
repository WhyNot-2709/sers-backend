package com.sers.sers_backend.controller;

import com.sers.sers_backend.dto.*;
import com.sers.sers_backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;

    @GetMapping("/{id}")
    public StudentDTO getStudent(@PathVariable Integer id) {
        return studentService.getStudent(id);
    }

    @GetMapping("/{id}/timetable")
    public List<CourseDTO> getTimetable(@PathVariable Integer id) {
        return courseService.getStudentTimetable(id);
    }

    @GetMapping("/{id}/electives")
    public List<CourseDTO> getAvailableElectives(@PathVariable Integer id) {
        return courseService.getAvailableElectives(id);
    }

    @PostMapping("/selection")
    public Map<String, Object> handleSelection(@RequestBody SelectionRequest request) {
        return courseService.handleSelection(
                request.getStudentId(),
                request.getCourseId(),
                request.getAction()
        );
    }
}