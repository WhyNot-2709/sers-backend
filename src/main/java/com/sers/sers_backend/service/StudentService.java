package com.sers.sers_backend.service;

import com.sers.sers_backend.dto.StudentDTO;
import com.sers.sers_backend.entity.Student;
import com.sers.sers_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired private StudentRepository studentRepo;

    public StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setName(student.getName());
        dto.setYear(student.getYear());
        dto.setSemester(student.getSemester());
        dto.setStream(student.getStream());
        dto.setCgpa(student.getCgpa());
        dto.setMaxCoreElectives(student.getMaxCoreElectives());
        dto.setMaxHumElectives(student.getMaxHumElectives());
        return dto;
    }

    public StudentDTO getStudent(Integer id) {
        return toDTO(studentRepo.findById(id).orElseThrow());
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
}