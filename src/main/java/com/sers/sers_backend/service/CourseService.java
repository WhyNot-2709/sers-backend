package com.sers.sers_backend.service;

import com.sers.sers_backend.dto.CourseDTO;
import com.sers.sers_backend.entity.*;
import com.sers.sers_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired private CourseRepository courseRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private StudentFixedCourseRepository fixedCourseRepo;
    @Autowired private ElectiveSelectionRepository selectionRepo;

    public CourseDTO toDTO(Course course, String status) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setType(course.getType());
        dto.setStreamCode(course.getStreamCode());
        dto.setCredits(course.getCredits());
        dto.setDays(course.getDays());
        dto.setStartTime(course.getStartTime().toString());
        dto.setEndTime(course.getEndTime().toString());
        dto.setClassroom(course.getClassroom());
        dto.setMaxSeats(course.getMaxSeats());
        dto.setCurrentSeats(course.getCurrentSeats());
        dto.setIsActive(course.getIsActive());
        dto.setDescription(course.getDescription());
        dto.setIsFixed(course.getIsFixed());
        dto.setStatus(status);
        if (course.getFaculty() != null) {
            dto.setFacultyName(course.getFaculty().getName());
            dto.setFacultyId(course.getFaculty().getId());
        }
        return dto;
    }

    public List<CourseDTO> getStudentTimetable(Integer studentId) {
        Student student = studentRepo.findById(studentId).orElseThrow();
        List<StudentFixedCourse> fixed = fixedCourseRepo.findByStudent(student);
        List<ElectiveSelection> selections = selectionRepo.findByStudent(student);

        List<CourseDTO> result = new ArrayList<>();

        for (StudentFixedCourse sfc : fixed) {
            result.add(toDTO(sfc.getCourse(), "FIXED"));
        }

        for (ElectiveSelection sel : selections) {
            result.add(toDTO(sel.getCourse(), sel.getStatus()));
        }

        return result;
    }

    public List<CourseDTO> getAvailableElectives(Integer studentId) {
        Student student = studentRepo.findById(studentId).orElseThrow();

        // Get all fixed and confirmed course slots for conflict check
        List<StudentFixedCourse> fixed = fixedCourseRepo.findByStudent(student);
        List<ElectiveSelection> confirmed = selectionRepo.findByStudentAndStatus(student, "CONFIRMED");

        Set<Integer> alreadySelectedIds = selectionRepo.findByStudent(student)
                .stream().map(s -> s.getCourse().getId()).collect(Collectors.toSet());

        List<Course> allElectives = courseRepo.findByIsFixedFalseAndIsActiveTrue();

        return allElectives.stream()
                .filter(course -> !alreadySelectedIds.contains(course.getId()))
                .filter(course -> !hasConflict(course, fixed, confirmed))
                .filter(course -> course.getCurrentSeats() < course.getMaxSeats())
                .map(course -> toDTO(course, null))
                .collect(Collectors.toList());
    }

    private boolean hasConflict(Course newCourse, List<StudentFixedCourse> fixed, List<ElectiveSelection> confirmed) {
        List<Course> occupied = new ArrayList<>();
        fixed.forEach(f -> occupied.add(f.getCourse()));
        confirmed.forEach(c -> occupied.add(c.getCourse()));

        Set<String> newDays = new HashSet<>(Arrays.asList(newCourse.getDays().split(",")));

        for (Course existing : occupied) {
            Set<String> existDays = new HashSet<>(Arrays.asList(existing.getDays().split(",")));
            existDays.retainAll(newDays);
            if (!existDays.isEmpty()) {
                // Same day — check time overlap
                if (timesOverlap(newCourse.getStartTime(), newCourse.getEndTime(),
                        existing.getStartTime(), existing.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public Map<String, Object> handleSelection(Integer studentId, Integer courseId, String action) {
        Student student = studentRepo.findById(studentId).orElseThrow();
        Course course = courseRepo.findById(courseId).orElseThrow();
        Map<String, Object> response = new HashMap<>();

        switch (action.toUpperCase()) {
            case "PREVIEW": {
                Optional<ElectiveSelection> existing = selectionRepo.findByStudentAndCourse(student, course);
                if (existing.isPresent()) {
                    response.put("success", false);
                    response.put("message", "Already selected");
                    break;
                }
                ElectiveSelection sel = new ElectiveSelection();
                sel.setStudent(student);
                sel.setCourse(course);
                sel.setStatus("PREVIEW");
                selectionRepo.save(sel);
                response.put("success", true);
                response.put("message", "Preview added");
                break;
            }
            case "CONFIRM": {
                Optional<ElectiveSelection> existing = selectionRepo.findByStudentAndCourse(student, course);
                if (existing.isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Not in preview");
                    break;
                }
                // Check seat availability
                if (course.getCurrentSeats() >= course.getMaxSeats()) {
                    response.put("success", false);
                    response.put("message", "No seats available");
                    break;
                }
                // Check limit
                long confirmedCount = selectionRepo.findByStudentAndStatus(student, "CONFIRMED").stream()
                        .filter(s -> s.getCourse().getType().equals(course.getType())).count();
                int limit = course.getType().equals("HUMANITIES") ?
                        student.getMaxHumElectives() : student.getMaxCoreElectives();
                if (confirmedCount >= limit) {
                    response.put("success", false);
                    response.put("message", "Selection limit reached");
                    break;
                }
                existing.get().setStatus("CONFIRMED");
                selectionRepo.save(existing.get());
                course.setCurrentSeats(course.getCurrentSeats() + 1);
                courseRepo.save(course);
                response.put("success", true);
                response.put("message", "Course confirmed");
                break;
            }
            case "REMOVE": {
                Optional<ElectiveSelection> existing = selectionRepo.findByStudentAndCourse(student, course);
                if (existing.isPresent()) {
                    if (existing.get().getStatus().equals("CONFIRMED")) {
                        course.setCurrentSeats(Math.max(0, course.getCurrentSeats() - 1));
                        courseRepo.save(course);
                    }
                    selectionRepo.delete(existing.get());
                }
                response.put("success", true);
                response.put("message", "Removed");
                break;
            }
        }
        return response;
    }

    public List<CourseDTO> getAllCoursesForAdmin() {
        return courseRepo.findAll().stream()
                .map(c -> toDTO(c, null))
                .collect(Collectors.toList());
    }

    public CourseDTO updateCourse(Integer courseId, CourseDTO dto) {
        Course course = courseRepo.findById(courseId).orElseThrow();
        course.setName(dto.getName());
        course.setCode(dto.getCode());
        course.setType(dto.getType());
        course.setCredits(dto.getCredits());
        course.setDays(dto.getDays());
        course.setStartTime(LocalTime.parse(dto.getStartTime()));
        course.setEndTime(LocalTime.parse(dto.getEndTime()));
        course.setClassroom(dto.getClassroom());
        course.setMaxSeats(dto.getMaxSeats());
        course.setIsActive(dto.getIsActive());
        course.setDescription(dto.getDescription());
        if (dto.getFacultyId() != null) {
            // faculty set separately
        }
        return toDTO(courseRepo.save(course), null);
    }

    public void deleteCourse(Integer courseId) {
        courseRepo.deleteById(courseId);
    }

    public CourseDTO createCourse(CourseDTO dto) {
        Course course = new Course();
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setType(dto.getType());
        course.setStreamCode(dto.getStreamCode());
        course.setCredits(dto.getCredits());
        course.setDays(dto.getDays());
        course.setStartTime(LocalTime.parse(dto.getStartTime()));
        course.setEndTime(LocalTime.parse(dto.getEndTime()));
        course.setClassroom(dto.getClassroom());
        course.setMaxSeats(dto.getMaxSeats() != null ? dto.getMaxSeats() : 25);
        course.setCurrentSeats(0);
        course.setIsActive(true);
        course.setDescription(dto.getDescription());
        course.setIsFixed(dto.getIsFixed() != null && dto.getIsFixed());
        return toDTO(courseRepo.save(course), null);
    }
}