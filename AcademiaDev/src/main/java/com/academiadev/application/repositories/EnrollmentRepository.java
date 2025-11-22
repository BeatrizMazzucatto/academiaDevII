package com.academiadev.application.repositories;

import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    void save(Enrollment enrollment);
    List<Enrollment> findAll();
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findActiveByStudent(Student student);
    int countActiveByStudent(Student student);
    Optional<Enrollment> findByStudentAndCourse(Student student, com.academiadev.domain.entities.Course course);
}

