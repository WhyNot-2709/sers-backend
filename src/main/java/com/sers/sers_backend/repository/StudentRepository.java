package com.sers.sers_backend.repository;

import com.sers.sers_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByRollNumber(String rollNumber);
}