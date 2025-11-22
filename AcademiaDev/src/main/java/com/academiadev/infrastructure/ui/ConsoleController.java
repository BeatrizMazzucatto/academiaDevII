package com.academiadev.infrastructure.ui;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.application.repositories.UserRepository;
import com.academiadev.application.usecases.*;
import com.academiadev.domain.entities.*;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;
import com.academiadev.domain.exceptions.BusinessException;
import com.academiadev.domain.exceptions.EnrollmentException;
import com.academiadev.infrastructure.utils.GenericCsvExporter;

import java.util.*;

public class ConsoleController {
    private final ConsoleView view;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    private final MatricularAlunoUseCase matricularAlunoUseCase;
    private final AtualizarProgressoUseCase atualizarProgressoUseCase;
    private final CancelarMatriculaUseCase cancelarMatriculaUseCase;
    private final AbrirTicketUseCase abrirTicketUseCase;
    private final AtenderTicketUseCase atenderTicketUseCase;
    private final GerarRelatorioCursosPorNivelUseCase relatorioCursosPorNivelUseCase;
    private final GerarRelatorioInstrutoresUnicosUseCase relatorioInstrutoresUnicosUseCase;
    private final GerarRelatorioAlunosPorPlanoUseCase relatorioAlunosPorPlanoUseCase;
    private final CalcularMediaProgressoUseCase calcularMediaProgressoUseCase;
    private final IdentificarAlunoMaisMatriculadoUseCase identificarAlunoMaisMatriculadoUseCase;
    private final AtualizarStatusCursoUseCase atualizarStatusCursoUseCase;
    private final AlterarPlanoAlunoUseCase alterarPlanoAlunoUseCase;
    
    private User currentUser;
    
    public ConsoleController(ConsoleView view, UserRepository userRepository,
                             CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository,
                             MatricularAlunoUseCase matricularAlunoUseCase,
                             AtualizarProgressoUseCase atualizarProgressoUseCase,
                             CancelarMatriculaUseCase cancelarMatriculaUseCase,
                             AbrirTicketUseCase abrirTicketUseCase,
                             AtenderTicketUseCase atenderTicketUseCase,
                             GerarRelatorioCursosPorNivelUseCase relatorioCursosPorNivelUseCase,
                             GerarRelatorioInstrutoresUnicosUseCase relatorioInstrutoresUnicosUseCase,
                             GerarRelatorioAlunosPorPlanoUseCase relatorioAlunosPorPlanoUseCase,
                             CalcularMediaProgressoUseCase calcularMediaProgressoUseCase,
                             IdentificarAlunoMaisMatriculadoUseCase identificarAlunoMaisMatriculadoUseCase,
                             AtualizarStatusCursoUseCase atualizarStatusCursoUseCase,
                             AlterarPlanoAlunoUseCase alterarPlanoAlunoUseCase) {
        this.view = view;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.matricularAlunoUseCase = matricularAlunoUseCase;
        this.atualizarProgressoUseCase = atualizarProgressoUseCase;
        this.cancelarMatriculaUseCase = cancelarMatriculaUseCase;
        this.abrirTicketUseCase = abrirTicketUseCase;
        this.atenderTicketUseCase = atenderTicketUseCase;
        this.relatorioCursosPorNivelUseCase = relatorioCursosPorNivelUseCase;
        this.relatorioInstrutoresUnicosUseCase = relatorioInstrutoresUnicosUseCase;
        this.relatorioAlunosPorPlanoUseCase = relatorioAlunosPorPlanoUseCase;
        this.calcularMediaProgressoUseCase = calcularMediaProgressoUseCase;
        this.identificarAlunoMaisMatriculadoUseCase = identificarAlunoMaisMatriculadoUseCase;
        this.atualizarStatusCursoUseCase = atualizarStatusCursoUseCase;
        this.alterarPlanoAlunoUseCase = alterarPlanoAlunoUseCase;
    }
    
    public void start() {
        view.showWelcome();
        authenticate();
        mainLoop();
    }
    
    private void authenticate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite seu email para autenticar: ");
        String email = scanner.nextLine().trim();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            view.showError("Usuário não encontrado!");
            System.exit(0);
        }
        
        currentUser = userOpt.get();
        String userType = currentUser.isAdmin() ? "Administrador" : "Aluno";
        view.showSuccess("Autenticado como " + userType + ": " + currentUser.getName());
    }
    
    private void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            view.showMainMenu(currentUser.isAdmin());
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                if (currentUser.isAdmin()) {
                    running = handleAdminMenu(choice, scanner);
                } else {
                    running = handleStudentMenu(choice, scanner);
                }
            } catch (EnrollmentException | BusinessException e) {
                view.showError(e.getMessage());
            } catch (Exception e) {
                view.showError("Erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (running) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }
        
        view.showMessage("Obrigado por usar a AcademiaDev! Até logo!");
    }
    
    private boolean handleAdminMenu(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                listCourses();
                break;
            case 2:
                openSupportTicket(scanner);
                break;
            case 3:
                manageCourseStatus(scanner);
                break;
            case 4:
                manageStudentPlan(scanner);
                break;
            case 5:
                processSupportTicket();
                break;
            case 6:
                showReportsMenu(scanner);
                break;
            case 7:
                exportToCsv(scanner);
                break;
            case 0:
                return false;
            default:
                view.showError("Opção inválida!");
        }
        return true;
    }
    
    private boolean handleStudentMenu(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                listCourses();
                break;
            case 2:
                openSupportTicket(scanner);
                break;
            case 3:
                enrollInCourse(scanner);
                break;
            case 4:
                listMyEnrollments();
                break;
            case 5:
                updateProgress(scanner);
                break;
            case 6:
                cancelEnrollment(scanner);
                break;
            case 0:
                return false;
            default:
                view.showError("Opção inválida!");
        }
        return true;
    }
    
    private void listCourses() {
        List<Course> activeCourses = courseRepository.findActiveCourses();
        view.showCourses(activeCourses);
    }
    
    private void enrollInCourse(Scanner scanner) {
        if (!(currentUser instanceof Student)) {
            view.showError("Apenas alunos podem se matricular!");
            return;
        }
        
        Student student = (Student) currentUser;
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine().trim();
        
        try {
            Enrollment enrollment = matricularAlunoUseCase.execute(student, courseTitle);
            view.showSuccess("Matriculado com sucesso no curso: " + courseTitle);
        } catch (EnrollmentException e) {
            view.showError(e.getMessage());
        }
    }
    
    private void listMyEnrollments() {
        if (!(currentUser instanceof Student)) {
            view.showError("Apenas alunos podem consultar matrículas!");
            return;
        }
        
        Student student = (Student) currentUser;
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        view.showEnrollments(enrollments);
    }
    
    private void updateProgress(Scanner scanner) {
        if (!(currentUser instanceof Student)) {
            view.showError("Apenas alunos podem atualizar progresso!");
            return;
        }
        
        Student student = (Student) currentUser;
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine().trim();
        System.out.print("Digite o progresso (0-100): ");
        double progress = scanner.nextDouble();
        scanner.nextLine();
        
        try {
            atualizarProgressoUseCase.execute(student, courseTitle, progress);
            view.showSuccess("Progresso atualizado com sucesso!");
        } catch (EnrollmentException e) {
            view.showError(e.getMessage());
        }
    }
    
    private void cancelEnrollment(Scanner scanner) {
        if (!(currentUser instanceof Student)) {
            view.showError("Apenas alunos podem cancelar matrículas!");
            return;
        }
        
        Student student = (Student) currentUser;
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine().trim();
        
        try {
            cancelarMatriculaUseCase.execute(student, courseTitle);
            view.showSuccess("Matrícula cancelada com sucesso!");
        } catch (EnrollmentException e) {
            view.showError(e.getMessage());
        }
    }
    
    private void openSupportTicket(Scanner scanner) {
        System.out.print("Digite o título do ticket: ");
        String title = scanner.nextLine().trim();
        System.out.print("Digite a mensagem: ");
        String message = scanner.nextLine().trim();
        
        SupportTicket ticket = abrirTicketUseCase.execute(title, message, currentUser);
        view.showSuccess("Ticket criado com sucesso!");
    }
    
    private void manageCourseStatus(Scanner scanner) {
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine().trim();
        System.out.print("Digite o novo status (ACTIVE/INACTIVE): ");
        String statusStr = scanner.nextLine().trim().toUpperCase();
        
        CourseStatus status;
        try {
            status = CourseStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            view.showError("Status inválido! Use ACTIVE ou INACTIVE.");
            return;
        }
        
        try {
            Course course = atualizarStatusCursoUseCase.execute(courseTitle, status);
            view.showSuccess("Status do curso atualizado com sucesso!");
        } catch (BusinessException e) {
            view.showError(e.getMessage());
        }
    }
    
    private void manageStudentPlan(Scanner scanner) {
        System.out.print("Digite o email do aluno: ");
        String email = scanner.nextLine().trim();
        System.out.print("Digite o novo plano (BASIC/PREMIUM): ");
        String planStr = scanner.nextLine().trim().toUpperCase();
        
        SubscriptionPlan plan;
        if ("BASIC".equals(planStr)) {
            plan = new BasicPlan();
        } else if ("PREMIUM".equals(planStr)) {
            plan = new PremiumPlan();
        } else {
            view.showError("Plano inválido! Use BASIC ou PREMIUM.");
            return;
        }
        
        try {
            Student student = alterarPlanoAlunoUseCase.execute(email, plan);
            view.showSuccess("Plano do aluno atualizado com sucesso!");
        } catch (BusinessException e) {
            view.showError(e.getMessage());
        }
    }
    
    private void processSupportTicket() {
        try {
            SupportTicket ticket = atenderTicketUseCase.execute();
            view.showMessage("\n=== TICKET ATENDIDO ===");
            view.showMessage("Título: " + ticket.getTitle());
            view.showMessage("Mensagem: " + ticket.getMessage());
            view.showMessage("Usuário: " + ticket.getUser().getEmail());
        } catch (BusinessException e) {
            view.showError(e.getMessage());
        }
    }
    
    private void showReportsMenu(Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            view.showAdminReportsMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    showCoursesByDifficulty(scanner);
                    break;
                case 2:
                    showUniqueInstructors();
                    break;
                case 3:
                    showStudentsByPlan();
                    break;
                case 4:
                    showAverageProgress();
                    break;
                case 5:
                    showTopEnrolledStudent();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    view.showError("Opção inválida!");
            }
            
            if (!back) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    private void showCoursesByDifficulty(Scanner scanner) {
        System.out.print("Digite o nível (BEGINNER/INTERMEDIATE/ADVANCED): ");
        String levelStr = scanner.nextLine().trim().toUpperCase();
        
        try {
            DifficultyLevel level = DifficultyLevel.valueOf(levelStr);
            List<Course> courses = relatorioCursosPorNivelUseCase.execute(level);
            view.showCoursesByDifficulty(courses, levelStr);
        } catch (IllegalArgumentException e) {
            view.showError("Nível inválido!");
        }
    }
    
    private void showUniqueInstructors() {
        Set<String> instructors = relatorioInstrutoresUnicosUseCase.execute();
        view.showUniqueInstructors(instructors);
    }
    
    private void showStudentsByPlan() {
        Map<SubscriptionPlan, List<Student>> studentsByPlan = relatorioAlunosPorPlanoUseCase.execute();
        view.showStudentsByPlan(studentsByPlan);
    }
    
    private void showAverageProgress() {
        double average = calcularMediaProgressoUseCase.execute();
        view.showAverageProgress(average);
    }
    
    private void showTopEnrolledStudent() {
        Optional<Student> student = identificarAlunoMaisMatriculadoUseCase.execute();
        view.showTopEnrolledStudent(student);
    }
    
    private void exportToCsv(Scanner scanner) {
        System.out.println("Selecione o tipo de dados para exportar:");
        System.out.println("[1] Cursos");
        System.out.println("[2] Alunos");
        System.out.print("Escolha: ");
        int type = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Digite os campos separados por vírgula (ou ENTER para todos): ");
        String fieldsInput = scanner.nextLine().trim();
        List<String> fields = fieldsInput.isEmpty() ? 
            new ArrayList<>() : 
            Arrays.asList(fieldsInput.split(","));
        
        String csv = "";
        
        if (type == 1) {
            List<Course> courses = courseRepository.findAll();
            csv = GenericCsvExporter.exportToCsv(courses, fields);
        } else if (type == 2) {
            List<User> students = userRepository.findAllStudents();
            csv = GenericCsvExporter.exportToCsv(students, fields);
        } else {
            view.showError("Tipo inválido!");
            return;
        }
        
        view.showCsvExport(csv);
    }
}

