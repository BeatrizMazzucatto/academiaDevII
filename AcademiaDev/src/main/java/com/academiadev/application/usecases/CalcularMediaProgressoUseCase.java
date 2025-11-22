package com.academiadev.application.usecases;

import com.academiadev.application.repositories.EnrollmentRepository;

import java.util.OptionalDouble;

public class CalcularMediaProgressoUseCase {
    private final EnrollmentRepository enrollmentRepository;
    
    public CalcularMediaProgressoUseCase(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }
    
    public double execute() {
        OptionalDouble average = enrollmentRepository.findAll()
            .stream()
            .mapToDouble(enrollment -> enrollment.getProgress())
            .average();
        
        return average.orElse(0.0);
    }
}

