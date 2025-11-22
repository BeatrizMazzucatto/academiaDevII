package com.academiadev.main;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.*;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;

public class InitialData {
    
    public static void populate(UserRepository userRepository, CourseRepository courseRepository) {
        populateUsers(userRepository);
        populateCourses(courseRepository);
    }
    
    private static void populateUsers(UserRepository userRepository) {
        Admin admin1 = new Admin("João Admin", "admin@academiadev.com");
        userRepository.save(admin1);
        
        Student student1 = new Student("Maria Silva", "maria@email.com", new BasicPlan());
        userRepository.save(student1);
        
        Student student2 = new Student("Pedro Santos", "pedro@email.com", new PremiumPlan());
        userRepository.save(student2);
        
        Student student3 = new Student("Ana Costa", "ana@email.com", new BasicPlan());
        userRepository.save(student3);
        
        Student student4 = new Student("Carlos Oliveira", "carlos@email.com", new PremiumPlan());
        userRepository.save(student4);
    }
    
    private static void populateCourses(CourseRepository courseRepository) {
        Course course1 = new Course(
            "Java Básico",
            "Aprenda os fundamentos da linguagem Java",
            "Prof. Roberto Silva",
            40,
            DifficultyLevel.BEGINNER,
            CourseStatus.ACTIVE
        );
        courseRepository.save(course1);
        
        Course course2 = new Course(
            "Java Avançado",
            "Domine recursos avançados do Java",
            "Prof. Roberto Silva",
            60,
            DifficultyLevel.ADVANCED,
            CourseStatus.ACTIVE
        );
        courseRepository.save(course2);
        
        Course course3 = new Course(
            "Spring Boot",
            "Desenvolva aplicações web com Spring Boot",
            "Prof. Ana Paula",
            50,
            DifficultyLevel.INTERMEDIATE,
            CourseStatus.ACTIVE
        );
        courseRepository.save(course3);
        
        Course course4 = new Course(
            "Python para Iniciantes",
            "Introdução à programação com Python",
            "Prof. Carlos Mendes",
            30,
            DifficultyLevel.BEGINNER,
            CourseStatus.ACTIVE
        );
        courseRepository.save(course4);
        
        Course course5 = new Course(
            "Docker e Kubernetes",
            "Containerização e orquestração de aplicações",
            "Prof. Ana Paula",
            45,
            DifficultyLevel.ADVANCED,
            CourseStatus.ACTIVE
        );
        courseRepository.save(course5);
        
        Course course6 = new Course(
            "JavaScript Moderno",
            "ES6+ e desenvolvimento front-end moderno",
            "Prof. Roberto Silva",
            35,
            DifficultyLevel.INTERMEDIATE,
            CourseStatus.INACTIVE
        );
        courseRepository.save(course6);
        
        Course course7 = new Course(
            "Banco de Dados SQL",
            "Fundamentos de bancos de dados relacionais",
            "Prof. Carlos Mendes",
            25,
            DifficultyLevel.BEGINNER,
            CourseStatus.ACTIVE
        );
        courseRepository.save(course7);
    }
}

