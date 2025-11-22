package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;

import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentRepositoryEmMemoria implements EnrollmentRepository {
    private final List<Enrollment> enrollments;
    
    public EnrollmentRepositoryEmMemoria() {
        this.enrollments = new ArrayList<>();
    }
    
    @Override
    public void save(Enrollment enrollment) {
        enrollments.add(enrollment);
    }
    
    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments);
    }
    
    @Override
    public List<Enrollment> findByStudent(Student student) {
        return enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> findActiveByStudent(Student student) {
        return enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .filter(Enrollment::isActive)
            .collect(Collectors.toList());
    }
    
    @Override
    public int countActiveByStudent(Student student) {
        return (int) enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .filter(Enrollment::isActive)
            .count();
    }
    
    @Override
    public Optional<Enrollment> findByStudentAndCourse(Student student, Course course) {
        return enrollments.stream()
            .filter(e -> e.getStudent().getEmail().equals(student.getEmail()))
            .filter(e -> e.getCourse().getTitle().equals(course.getTitle()))
            .findFirst();
    }
}

