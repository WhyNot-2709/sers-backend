package com.sers.sers_backend.service;

import com.sers.sers_backend.dto.CourseDTO;
import com.sers.sers_backend.dto.StudentDTO;
import com.sers.sers_backend.entity.*;
import com.sers.sers_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired private FacultyRepository facultyRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private ElectiveSelectionRepository selectionRepo;
    @Autowired private CourseService courseService;
    @Autowired private StudentService studentService;

    public List<CourseDTO> getCoursesForProfessor(Integer facultyId) {
        Faculty faculty = facultyRepo.findById(facultyId).orElseThrow();
        return courseRepo.findAll().stream()
                .filter(c -> c.getFaculty() != null && c.getFaculty().getId().equals(facultyId))
                .map(c -> courseService.toDTO(c, null))
                .collect(Collectors.toList());
    }

    public List<StudentDTO> getStudentsForProfessorCourse(Integer courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow();
        return selectionRepo.findByCourseAndStatus(course, "CONFIRMED").stream()
                .map(sel -> studentService.toDTO(sel.getStudent()))
                .collect(Collectors.toList());
    }
}