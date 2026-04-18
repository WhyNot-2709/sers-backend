package com.sers.sers_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_fixed_courses")
@Data
public class StudentFixedCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}