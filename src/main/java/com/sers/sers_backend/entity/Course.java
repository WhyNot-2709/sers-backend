package com.sers.sers_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // CORE, HUMANITIES, FIXED

    @Column(name = "stream_code")
    private String streamCode;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private String days; // "MON,WED,FRI"

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    private String classroom;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "max_seats", nullable = false)
    private Integer maxSeats;

    @Column(name = "current_seats")
    private Integer currentSeats = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private String description;

    @Column(name = "is_fixed")
    private Boolean isFixed = false;
}