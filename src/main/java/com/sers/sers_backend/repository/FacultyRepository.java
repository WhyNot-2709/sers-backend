package com.sers.sers_backend.repository;

import com.sers.sers_backend.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    Optional<Faculty> findByEmail(String email);
}