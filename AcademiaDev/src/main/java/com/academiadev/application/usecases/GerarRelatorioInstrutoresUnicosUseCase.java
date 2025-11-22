package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class GerarRelatorioInstrutoresUnicosUseCase {
    private final CourseRepository courseRepository;
    
    public GerarRelatorioInstrutoresUnicosUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    public Set<String> execute() {
        return courseRepository.findActiveCourses()
            .stream()
            .map(course -> course.getInstructorName())
            .collect(Collectors.toSet());
    }
}

