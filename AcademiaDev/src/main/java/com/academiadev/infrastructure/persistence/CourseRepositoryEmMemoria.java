package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;

import java.util.*;
import java.util.stream.Collectors;

public class CourseRepositoryEmMemoria implements CourseRepository {
    private final Map<String, Course> courses;
    
    public CourseRepositoryEmMemoria() {
        this.courses = new HashMap<>();
    }
    
    @Override
    public void save(Course course) {
        courses.put(course.getTitle(), course);
    }
    
    @Override
    public Optional<Course> findByTitle(String title) {
        return Optional.ofNullable(courses.get(title));
    }
    
    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public List<Course> findByDifficultyLevel(DifficultyLevel difficultyLevel) {
        return courses.values()
            .stream()
            .filter(course -> course.getDifficultyLevel() == difficultyLevel)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findActiveCourses() {
        return courses.values()
            .stream()
            .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
            .collect(Collectors.toList());
    }
}

