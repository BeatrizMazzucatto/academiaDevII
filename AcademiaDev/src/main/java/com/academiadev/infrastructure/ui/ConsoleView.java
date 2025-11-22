package com.academiadev.infrastructure.ui;

import com.academiadev.domain.entities.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ConsoleView {
    
    public void showWelcome() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     Bem-vindo à AcademiaDev - Plataforma de Cursos         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
    
    public void showMainMenu(boolean isAdmin) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                      MENU PRINCIPAL                           ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("\n[1] Consultar Catálogo de Cursos");
        System.out.println("[2] Abrir Ticket de Suporte");
        
        if (isAdmin) {
            System.out.println("\n--- OPERAÇÕES DE ADMINISTRADOR ---");
            System.out.println("[3] Gerenciar Status de Cursos");
            System.out.println("[4] Gerenciar Planos de Alunos");
            System.out.println("[5] Atender Tickets de Suporte");
            System.out.println("[6] Gerar Relatórios e Análises");
            System.out.println("[7] Exportar Dados para CSV");
        } else {
            System.out.println("\n--- OPERAÇÕES DO ALUNO ---");
            System.out.println("[3] Matricular-se em Curso");
            System.out.println("[4] Consultar Minhas Matrículas");
            System.out.println("[5] Atualizar Progresso");
            System.out.println("[6] Cancelar Matrícula");
        }
        
        System.out.println("\n[0] Sair");
        System.out.print("\nEscolha uma opção: ");
    }
    
    public void showAdminReportsMenu() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                  RELATÓRIOS E ANÁLISES                        ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("[1] Cursos por Nível de Dificuldade");
        System.out.println("[2] Instrutores Únicos de Cursos Ativos");
        System.out.println("[3] Alunos Agrupados por Plano");
        System.out.println("[4] Média Geral de Progresso");
        System.out.println("[5] Aluno com Mais Matrículas Ativas");
        System.out.println("[0] Voltar");
        System.out.print("\nEscolha uma opção: ");
    }
    
    public void showCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("\nNenhum curso encontrado.");
            return;
        }
        
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                      CATÁLOGO DE CURSOS                       ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.println((i + 1) + ". " + course.getTitle());
            System.out.println("   Instrutor: " + course.getInstructorName());
            System.out.println("   Nível: " + course.getDifficultyLevel());
            System.out.println("   Carga Horária: " + course.getDurationInHours() + "h");
            System.out.println("   Status: " + course.getStatus());
            System.out.println("   Descrição: " + course.getDescription());
            System.out.println();
        }
    }
    
    public void showEnrollments(List<Enrollment> enrollments) {
        if (enrollments.isEmpty()) {
            System.out.println("\nNenhuma matrícula encontrada.");
            return;
        }
        
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                      MINHAS MATRÍCULAS                        ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            System.out.println((i + 1) + ". " + enrollment.getCourse().getTitle());
            System.out.println("   Progresso: " + String.format("%.1f", enrollment.getProgress()) + "%");
            System.out.println("   Status: " + (enrollment.isActive() ? "Ativa" : "Cancelada"));
            System.out.println();
        }
    }
    
    public void showCoursesByDifficulty(List<Course> courses, String difficulty) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("          CURSOS - NÍVEL: " + difficulty);
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        if (courses.isEmpty()) {
            System.out.println("Nenhum curso encontrado para este nível.");
            return;
        }
        
        for (Course course : courses) {
            System.out.println("• " + course.getTitle() + " - " + course.getInstructorName());
        }
    }
    
    public void showUniqueInstructors(Set<String> instructors) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("              INSTRUTORES ÚNICOS (CURSOS ATIVOS)               ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        if (instructors.isEmpty()) {
            System.out.println("Nenhum instrutor encontrado.");
            return;
        }
        
        instructors.forEach(instructor -> System.out.println("• " + instructor));
        System.out.println("\nTotal: " + instructors.size() + " instrutor(es)");
    }
    
    public void showStudentsByPlan(Map<SubscriptionPlan, List<Student>> studentsByPlan) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("              ALUNOS AGRUPADOS POR PLANO                       ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        if (studentsByPlan.isEmpty()) {
            System.out.println("Nenhum aluno encontrado.");
            return;
        }
        
        studentsByPlan.forEach((plan, students) -> {
            System.out.println("\n📋 Plano: " + plan.getName());
            System.out.println("   Total de alunos: " + students.size());
            students.forEach(student -> 
                System.out.println("   • " + student.getName() + " (" + student.getEmail() + ")")
            );
        });
    }
    
    public void showAverageProgress(double average) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("              MÉDIA GERAL DE PROGRESSO                         ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        System.out.println("Média: " + String.format("%.2f", average) + "%");
    }
    
    public void showTopEnrolledStudent(Optional<Student> student) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("        ALUNO COM MAIS MATRÍCULAS ATIVAS                       ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        if (student.isPresent()) {
            Student s = student.get();
            System.out.println("Aluno: " + s.getName());
            System.out.println("Email: " + s.getEmail());
            System.out.println("Plano: " + s.getSubscriptionPlan().getName());
        } else {
            System.out.println("Nenhum aluno com matrículas ativas encontrado.");
        }
    }
    
    public void showCsvExport(String csv) {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                    EXPORTAÇÃO CSV                              ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        System.out.println(csv);
    }
    
    public void showMessage(String message) {
        System.out.println("\n" + message);
    }
    
    public void showError(String error) {
        System.err.println("\n❌ ERRO: " + error);
    }
    
    public void showSuccess(String message) {
        System.out.println("\n✅ " + message);
    }
    
    public void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}

