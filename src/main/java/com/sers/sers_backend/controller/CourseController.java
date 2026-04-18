package com.sers.sers_backend.controller;

import com.sers.sers_backend.dto.CourseDTO;
import com.sers.sers_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired private CourseService courseService;

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCoursesForAdmin();
    }

    @PostMapping
    public CourseDTO createCourse(@RequestBody CourseDTO dto) {
        return courseService.createCourse(dto);
    }

    @PutMapping("/{id}")
    public CourseDTO updateCourse(@PathVariable Integer id, @RequestBody CourseDTO dto) {
        return courseService.updateCourse(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
    }
}