package com.sers.sers_backend.repository;

import com.sers.sers_backend.entity.Allocation;
import com.sers.sers_backend.entity.Course;
import com.sers.sers_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Integer> {
    List<Allocation> findByStudent(Student student);
    List<Allocation> findByCourse(Course course);
}