package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.enums.DifficultyLevel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GerarRelatorioCursosPorNivelUseCase {
    private final CourseRepository courseRepository;
    
    public GerarRelatorioCursosPorNivelUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    public List<Course> execute(DifficultyLevel difficultyLevel) {
        return courseRepository.findByDifficultyLevel(difficultyLevel)
            .stream()
            .sorted(Comparator.comparing(Course::getTitle))
            .collect(Collectors.toList());
    }
}

