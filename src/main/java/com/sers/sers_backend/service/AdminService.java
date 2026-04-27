package com.sers.sers_backend.service;

import com.sers.sers_backend.dto.CourseDTO;
import com.sers.sers_backend.dto.StudentDTO;
import com.sers.sers_backend.entity.*;
import com.sers.sers_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for data safety
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired private CourseRepository courseRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private ElectiveSelectionRepository selectionRepo;
    @Autowired private AllocationRepository allocationRepo;
    @Autowired private CourseService courseService;
    @Autowired private StudentService studentService;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentRepo.count());
        stats.put("totalCourses", courseRepo.count());
        stats.put("totalConfirmed", selectionRepo.findAll().stream()
                .filter(s -> s.getStatus().equals("CONFIRMED")).count());

        List<Map<String, Object>> courseStats = courseRepo.findByIsFixedFalseAndIsActiveTrue().stream()
                .map(course -> {
                    Map<String, Object> cs = new HashMap<>();
                    cs.put("courseId", course.getId());
                    cs.put("courseName", course.getName());
                    cs.put("courseCode", course.getCode());
                    cs.put("maxSeats", course.getMaxSeats());
                    cs.put("currentSeats", course.getCurrentSeats());
                    cs.put("fillPercentage", course.getMaxSeats() > 0 ?
                            (course.getCurrentSeats() * 100.0 / course.getMaxSeats()) : 0);
                    return cs;
                }).collect(Collectors.toList());

        stats.put("courseStats", courseStats);
        return stats;
    }

    @Transactional
    public Map<String, Object> overrideAllocation(Integer studentId, Integer courseId) {
        Student student = studentRepo.findById(studentId).orElseThrow();
        Course course = courseRepo.findById(courseId).orElseThrow();

        Allocation allocation = new Allocation();
        allocation.setStudent(student);
        allocation.setCourse(course);
        allocation.setOverrideByAdmin(true);
        allocationRepo.save(allocation);

        // Also confirm the selection
        Optional<ElectiveSelection> sel = selectionRepo.findByStudentAndCourse(student, course);
        if (sel.isEmpty()) {
            ElectiveSelection newSel = new ElectiveSelection();
            newSel.setStudent(student);
            newSel.setCourse(course);
            newSel.setStatus("CONFIRMED");
            selectionRepo.save(newSel);
            course.setCurrentSeats(course.getCurrentSeats() + 1);
            courseRepo.save(course);
        } else {
            sel.get().setStatus("CONFIRMED");
            selectionRepo.save(sel.get());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Allocation overridden successfully");
        return response;
    }

    public List<StudentDTO> getStudentsForCourse(Integer courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow();
        return selectionRepo.findByCourseAndStatus(course, "CONFIRMED").stream()
                .map(sel -> studentService.toDTO(sel.getStudent()))
                .collect(Collectors.toList());
    }

    // NEW METHOD: Removes a student, drops their seat, and cleans up overrides
    @Transactional
    public Map<String, Object> removeStudentFromCourse(Integer studentId, Integer courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // 1. Remove the active course selection and free up the seat
        Optional<ElectiveSelection> selection = selectionRepo.findByStudentAndCourse(student, course);
        if (selection.isPresent()) {
            // Only decrement if the student actually held a confirmed seat
            if ("CONFIRMED".equals(selection.get().getStatus()) && course.getCurrentSeats() > 0) {
                course.setCurrentSeats(course.getCurrentSeats() - 1);
                courseRepo.save(course);
            }
            selectionRepo.delete(selection.get());
        }

        // 2. Clean up any manual Admin Allocations for this specific course/student combo
        List<Allocation> allocationsToRemove = allocationRepo.findAll().stream()
                .filter(a -> a.getStudent().getId().equals(studentId) && a.getCourse().getId().equals(courseId))
                .collect(Collectors.toList());

        if (!allocationsToRemove.isEmpty()) {
            allocationRepo.deleteAll(allocationsToRemove);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Student successfully removed from the course");
        return response;
    }
}