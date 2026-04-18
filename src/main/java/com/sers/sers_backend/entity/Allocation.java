package com.sers.sers_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "allocations")
@Data
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "allocated_at")
    private LocalDateTime allocatedAt = LocalDateTime.now();

    @Column(name = "override_by_admin")
    private Boolean overrideByAdmin = false;
}