package com.sers.sers_backend.repository;

import com.sers.sers_backend.entity.StudentFixedCourse;
import com.sers.sers_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentFixedCourseRepository extends JpaRepository<StudentFixedCourse, Integer> {
    List<StudentFixedCourse> findByStudent(Student student);
}