package com.academiadev.application.usecases;

import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IdentificarAlunoMaisMatriculadoUseCase {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    
    public IdentificarAlunoMaisMatriculadoUseCase(EnrollmentRepository enrollmentRepository,
                                                    UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }
    
    public Optional<Student> execute() {
        Map<Student, Long> enrollmentCounts = enrollmentRepository.findAll()
            .stream()
            .filter(Enrollment::isActive)
            .map(enrollment -> enrollment.getStudent())
            .collect(Collectors.groupingBy(
                student -> student,
                Collectors.counting()
            ));
        
        return enrollmentCounts.entrySet()
            .stream()
            .max(Comparator.comparing(Map.Entry::getValue))
            .map(Map.Entry::getKey);
    }
}

