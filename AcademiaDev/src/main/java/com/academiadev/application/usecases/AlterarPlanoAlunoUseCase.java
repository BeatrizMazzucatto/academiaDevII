package com.academiadev.application.usecases;

import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.entities.SubscriptionPlan;
import com.academiadev.domain.exceptions.BusinessException;

import java.util.Optional;

public class AlterarPlanoAlunoUseCase {
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    public AlterarPlanoAlunoUseCase(UserRepository userRepository, 
                                     EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    
    public Student execute(String studentEmail, SubscriptionPlan newPlan) {
        Optional<com.academiadev.domain.entities.User> userOpt = 
            userRepository.findByEmail(studentEmail);
        
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Student)) {
            throw new BusinessException("Aluno não encontrado: " + studentEmail);
        }
        
        Student student = (Student) userOpt.get();
        
        int activeEnrollments = enrollmentRepository.countActiveByStudent(student);
        
        if (!newPlan.canEnroll(activeEnrollments)) {
            throw new BusinessException(
                "Não é possível alterar para este plano. " +
                "Aluno possui " + activeEnrollments + " matrículas ativas " +
                "e o plano " + newPlan.getName() + " permite no máximo " + 
                newPlan.getMaxActiveEnrollments() + " matrículas.");
        }
        
        student.setSubscriptionPlan(newPlan);
        
        return student;
    }
}

