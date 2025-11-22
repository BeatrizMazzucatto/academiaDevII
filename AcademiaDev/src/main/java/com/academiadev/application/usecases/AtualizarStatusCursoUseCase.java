package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.exceptions.BusinessException;

import java.util.Optional;

public class AtualizarStatusCursoUseCase {
    private final CourseRepository courseRepository;
    
    public AtualizarStatusCursoUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    public Course execute(String courseTitle, CourseStatus newStatus) {
        Optional<Course> courseOpt = courseRepository.findByTitle(courseTitle);
        if (courseOpt.isEmpty()) {
            throw new BusinessException("Curso não encontrado: " + courseTitle);
        }
        
        Course course = courseOpt.get();
        course.setStatus(newStatus);
        
        return course;
    }
}

