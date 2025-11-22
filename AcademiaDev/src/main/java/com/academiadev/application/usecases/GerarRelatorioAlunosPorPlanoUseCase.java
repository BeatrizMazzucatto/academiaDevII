package com.academiadev.application.usecases;

import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.entities.SubscriptionPlan;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GerarRelatorioAlunosPorPlanoUseCase {
    private final UserRepository userRepository;
    
    public GerarRelatorioAlunosPorPlanoUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Map<SubscriptionPlan, List<Student>> execute() {
        return userRepository.findAllStudents()
            .stream()
            .filter(user -> user instanceof Student)
            .map(user -> (Student) user)
            .collect(Collectors.groupingBy(Student::getSubscriptionPlan));
    }
}

