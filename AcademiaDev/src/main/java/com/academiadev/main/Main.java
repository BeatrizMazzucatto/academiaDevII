package com.academiadev.main;

import com.academiadev.application.repositories.*;
import com.academiadev.application.usecases.*;
import com.academiadev.infrastructure.persistence.*;
import com.academiadev.infrastructure.ui.ConsoleController;
import com.academiadev.infrastructure.ui.ConsoleView;

public class Main {
    
    public static void main(String[] args) {
        CourseRepository courseRepository = new CourseRepositoryEmMemoria();
        UserRepository userRepository = new UserRepositoryEmMemoria();
        EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryEmMemoria();
        SupportTicketQueue ticketQueue = new SupportTicketQueueEmMemoria();
        
        InitialData.populate(userRepository, courseRepository);
        
        MatricularAlunoUseCase matricularAlunoUseCase = new MatricularAlunoUseCase(
            enrollmentRepository, courseRepository);
        
        AtualizarProgressoUseCase atualizarProgressoUseCase = new AtualizarProgressoUseCase(
            enrollmentRepository);
        
        CancelarMatriculaUseCase cancelarMatriculaUseCase = new CancelarMatriculaUseCase(
            enrollmentRepository);
        
        AbrirTicketUseCase abrirTicketUseCase = new AbrirTicketUseCase(ticketQueue);
        
        AtenderTicketUseCase atenderTicketUseCase = new AtenderTicketUseCase(ticketQueue);
        
        GerarRelatorioCursosPorNivelUseCase relatorioCursosPorNivelUseCase = 
            new GerarRelatorioCursosPorNivelUseCase(courseRepository);
        
        GerarRelatorioInstrutoresUnicosUseCase relatorioInstrutoresUnicosUseCase = 
            new GerarRelatorioInstrutoresUnicosUseCase(courseRepository);
        
        GerarRelatorioAlunosPorPlanoUseCase relatorioAlunosPorPlanoUseCase = 
            new GerarRelatorioAlunosPorPlanoUseCase(userRepository);
        
        CalcularMediaProgressoUseCase calcularMediaProgressoUseCase = 
            new CalcularMediaProgressoUseCase(enrollmentRepository);
        
        IdentificarAlunoMaisMatriculadoUseCase identificarAlunoMaisMatriculadoUseCase = 
            new IdentificarAlunoMaisMatriculadoUseCase(enrollmentRepository, userRepository);
        
        AtualizarStatusCursoUseCase atualizarStatusCursoUseCase = 
            new AtualizarStatusCursoUseCase(courseRepository);
        
        AlterarPlanoAlunoUseCase alterarPlanoAlunoUseCase = 
            new AlterarPlanoAlunoUseCase(userRepository, enrollmentRepository);
        
        ConsoleView view = new ConsoleView();
        
        ConsoleController controller = new ConsoleController(
            view,
            userRepository,
            courseRepository,
            enrollmentRepository,
            matricularAlunoUseCase,
            atualizarProgressoUseCase,
            cancelarMatriculaUseCase,
            abrirTicketUseCase,
            atenderTicketUseCase,
            relatorioCursosPorNivelUseCase,
            relatorioInstrutoresUnicosUseCase,
            relatorioAlunosPorPlanoUseCase,
            calcularMediaProgressoUseCase,
            identificarAlunoMaisMatriculadoUseCase,
            atualizarStatusCursoUseCase,
            alterarPlanoAlunoUseCase
        );
        
        controller.start();
    }
}

