package com.sers.sers_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roll_number", unique = true, nullable = false)
    private String rollNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private String stream;

    @Column(nullable = false)
    private BigDecimal cgpa;

    @Column(nullable = false)
    private String password;

    @Column(name = "max_core_electives", nullable = false)
    private Integer maxCoreElectives;

    @Column(name = "max_hum_electives", nullable = false)
    private Integer maxHumElectives;
}