package com.sers.sers_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "elective_selections")
@Data
public class ElectiveSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private String status; // PREVIEW or CONFIRMED

    @Column(name = "selected_at")
    private LocalDateTime selectedAt = LocalDateTime.now();
}