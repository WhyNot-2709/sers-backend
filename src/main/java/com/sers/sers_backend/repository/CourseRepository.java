package com.sers.sers_backend.repository;

import com.sers.sers_backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByIsFixedFalseAndIsActiveTrue();
    List<Course> findByTypeAndIsActiveTrue(String type);
    List<Course> findByIsFixedTrue();
}