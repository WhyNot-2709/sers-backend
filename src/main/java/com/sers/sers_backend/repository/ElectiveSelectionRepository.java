package com.sers.sers_backend.repository;

import com.sers.sers_backend.entity.ElectiveSelection;
import com.sers.sers_backend.entity.Student;
import com.sers.sers_backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ElectiveSelectionRepository extends JpaRepository<ElectiveSelection, Integer> {
    List<ElectiveSelection> findByStudent(Student student);
    List<ElectiveSelection> findByStudentAndStatus(Student student, String status);
    Optional<ElectiveSelection> findByStudentAndCourse(Student student, Course course);
    List<ElectiveSelection> findByCourseAndStatus(Course course, String status);
    long countByStudentAndStatusAndCourseType(Student student, String status, String type);
}