package com.academiadev.application.usecases;

import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.exceptions.EnrollmentException;

import java.util.Optional;

public class AtualizarProgressoUseCase {
    private final EnrollmentRepository enrollmentRepository;
    
    public AtualizarProgressoUseCase(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }
    
    public Enrollment execute(Student student, String courseTitle, double progress) {
        if (progress < 0 || progress > 100) {
            throw new EnrollmentException("Progresso deve estar entre 0 e 100");
        }
        
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
        enrollment.setProgress(progress);
        
        return enrollment;
    }
}

