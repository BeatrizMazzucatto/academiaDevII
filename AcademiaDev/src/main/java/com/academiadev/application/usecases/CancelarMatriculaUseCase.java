package com.academiadev.application.usecases;

import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.exceptions.EnrollmentException;

import java.util.Optional;

public class CancelarMatriculaUseCase {
    private final EnrollmentRepository enrollmentRepository;
    
    public CancelarMatriculaUseCase(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }
    
    public void execute(Student student, String courseTitle) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository
            .findActiveByStudent(student)
            .stream()
            .filter(e -> e.getCourse().getTitle().equals(courseTitle))
            .findFirst();
        
        if (enrollmentOpt.isEmpty()) {
            throw new EnrollmentException(
                "Aluno não está matriculado ou matrícula inativa no curso: " + courseTitle);
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        enrollment.cancel();
    }
}

