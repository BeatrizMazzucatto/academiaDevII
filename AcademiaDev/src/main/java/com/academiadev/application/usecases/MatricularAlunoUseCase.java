package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.exceptions.EnrollmentException;

import java.util.Optional;

public class MatricularAlunoUseCase {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    
    public MatricularAlunoUseCase(EnrollmentRepository enrollmentRepository, 
                                   CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }
    
    public Enrollment execute(Student student, String courseTitle) {
        Optional<Course> courseOpt = courseRepository.findByTitle(courseTitle);
        if (courseOpt.isEmpty()) {
            throw new EnrollmentException("Curso não encontrado: " + courseTitle);
        }
        
        Course course = courseOpt.get();
        
        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new EnrollmentException("Curso não está ativo: " + courseTitle);
        }
        
        Optional<Enrollment> existingEnrollment = enrollmentRepository
            .findByStudentAndCourse(student, course);
        
        if (existingEnrollment.isPresent() && existingEnrollment.get().isActive()) {
            throw new EnrollmentException("Aluno já está matriculado neste curso: " + courseTitle);
        }
        
        int activeEnrollments = enrollmentRepository.countActiveByStudent(student);
        
        if (!student.canEnroll(activeEnrollments)) {
            throw new EnrollmentException(
                "Limite de matrículas ativas atingido. Plano atual: " + 
                student.getSubscriptionPlan().getName());
        }
        
        Enrollment enrollment = new Enrollment(student, course);
        enrollmentRepository.save(enrollment);
        
        return enrollment;
    }
}

