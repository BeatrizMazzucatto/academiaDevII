# AcademiaDevII - Plataforma de Cursos Online

Este projeto implementa uma plataforma de cursos online robusta, escalável e de fácil manutenção, seguindo rigorosamente os princípios da **Clean Architecture** (Arquitetura Limpa) proposta por Robert C. Martin (Uncle Bob), com persistência em memória, programação funcional usando Streams, e exportação CSV com Reflection.

## 📋 Descrição

O AcademiaDev é um sistema de gerenciamento de cursos online que permite gerenciar catálogo de cursos, sistema de assinaturas (Basic e Premium), matrículas e acompanhamento de progresso, fila de tickets de suporte (FIFO), relatórios e análises da plataforma, e exportação de dados para CSV. A aplicação foi desenvolvida seguindo uma arquitetura em camadas (Domain-Application-Infrastructure-Main), garantindo total independência da lógica de negócio dos detalhes de implementação.

## 🗂️ Estrutura do Projeto

```
AcademiaDev/
├── src/main/java/com/academiadev/
│   ├── domain/                          # Camada Domain (Core - Lógica de Negócio)
│   │   ├── entities/
│   │   │   ├── Course.java
│   │   │   ├── User.java
│   │   │   ├── Admin.java
│   │   │   ├── Student.java
│   │   │   ├── SubscriptionPlan.java
│   │   │   ├── BasicPlan.java
│   │   │   ├── PremiumPlan.java
│   │   │   ├── Enrollment.java
│   │   │   └── SupportTicket.java
│   │   ├── enums/
│   │   │   ├── DifficultyLevel.java
│   │   │   └── CourseStatus.java
│   │   └── exceptions/
│   │       ├── BusinessException.java
│   │       └── EnrollmentException.java
│   │
│   ├── application/                     # Camada Application (Casos de Uso)
│   │   ├── usecases/
│   │   │   ├── MatricularAlunoUseCase.java
│   │   │   ├── AtualizarProgressoUseCase.java
│   │   │   ├── CancelarMatriculaUseCase.java
│   │   │   ├── AbrirTicketUseCase.java
│   │   │   ├── AtenderTicketUseCase.java
│   │   │   ├── GerarRelatorioCursosPorNivelUseCase.java
│   │   │   ├── GerarRelatorioInstrutoresUnicosUseCase.java
│   │   │   ├── GerarRelatorioAlunosPorPlanoUseCase.java
│   │   │   ├── CalcularMediaProgressoUseCase.java
│   │   │   ├── IdentificarAlunoMaisMatriculadoUseCase.java
│   │   │   ├── AtualizarStatusCursoUseCase.java
│   │   │   └── AlterarPlanoAlunoUseCase.java
│   │   └── repositories/                # Interfaces (Abstrações)
│   │       ├── CourseRepository.java
│   │       ├── UserRepository.java
│   │       ├── EnrollmentRepository.java
│   │       └── SupportTicketQueue.java
│   │
│   ├── infrastructure/                  # Camada Infrastructure (Detalhes)
│   │   ├── persistence/                 # Implementações em memória
│   │   │   ├── CourseRepositoryEmMemoria.java
│   │   │   ├── UserRepositoryEmMemoria.java
│   │   │   ├── EnrollmentRepositoryEmMemoria.java
│   │   │   └── SupportTicketQueueEmMemoria.java
│   │   ├── ui/                          # Interface de linha de comando
│   │   │   ├── ConsoleController.java
│   │   │   └── ConsoleView.java
│   │   └── utils/                       # Utilitários (Reflection)
│   │       └── GenericCsvExporter.java
│   │
│   └── main/                            # Camada Main (Composição)
│       ├── Main.java                    # Injeção de Dependência Manual
│       └── InitialData.java             # Dados Iniciais
│
├── pom.xml
└── README.md
```

## 🎯 Funcionalidades Implementadas

### Gerenciamento do Catálogo de Courses

**Objetivo:** Gerenciar cursos da plataforma com todas as características especificadas.

**Conceitos abordados:**
- Entidades de domínio puras (sem dependências externas)
- Enums para DifficultyLevel e CourseStatus
- Validação de regras de negócio (curso INACTIVE não aceita matrículas)
- Unicidade de título usando Map

**Implementação:**
- Cursos possuem: title (único), description, instructorName, durationInHours, difficultyLevel, status
- Repository em memória usando `Map<String, Course>` para garantir unicidade de título
- Status ACTIVE/INACTIVE controla se curso aceita matrículas

### Users e Subscription Plans

**Objetivo:** Gerenciar usuários (Admin e Student) com seus respectivos planos de assinatura.

**Conceitos abordados:**
- Herança e polimorfismo (User -> Admin/Student)
- Classe abstrata SubscriptionPlan com implementações concretas
- Validação de unicidade de email usando Map
- Lógica de negócio no domain (canEnroll)

**Implementação:**
- Admin: name, email
- Student: name, email, subscriptionPlan
- BasicPlan: máximo 3 matrículas ativas
- PremiumPlan: matrículas ilimitadas
- Lógica `student.canEnroll()` no domain

### Sistema de Enrollments e Progress

**Objetivo:** Gerenciar matrículas e acompanhamento de progresso dos alunos.

**Conceitos abordados:**
- Validação de regras de negócio no UseCase
- Verificação de limite de matrículas por plano
- Validação de status do curso
- Progresso de 0 a 100%

**Implementação:**
- Matrícula só permitida se plano permitir e curso estiver ACTIVE
- Progresso inicia em 0% e pode ser atualizado
- Validações no UseCase (camada application)

### Fila de Suporte ao User

**Objetivo:** Sistema de fila FIFO para atendimento de tickets de suporte.

**Conceitos abordados:**
- Fila FIFO usando Queue (ArrayDeque)
- Qualquer usuário pode abrir tickets
- Apenas Admin pode processar tickets
- Processamento em ordem de chegada

**Implementação:**
- `SupportTicketQueueEmMemoria` usa `ArrayDeque<SupportTicket>`
- Garante comportamento FIFO (First-In, First-Out)
- UseCases separados: AbrirTicketUseCase e AtenderTicketUseCase

### Relatórios e Análises da Plataforma

**Objetivo:** Gerar informações analíticas da plataforma usando Stream API.

**Conceitos abordados:**
- Programação funcional com Java Streams
- Operações de agrupamento e ordenação
- Cálculos estatísticos
- Relatórios implementados nos UseCases

**Implementação:**
- Cursos por difficultyLevel ordenados alfabeticamente
- Instrutores únicos usando `Set` (sem duplicatas)
- Alunos agrupados por subscriptionPlan usando `groupingBy`
- Média geral de progresso usando `average()`
- Aluno com mais matrículas ativas usando `max()`

### Exportação de Dados para CSV

**Objetivo:** Exportar dados da plataforma para CSV com campos selecionáveis.

**Conceitos abordados:**
- Reflection para acesso dinâmico a campos
- Isolamento de detalhes de framework na infrastructure
- Application não conhece GenericCsvExporter
- UI coordena entre UseCase e Exporter

**Implementação:**
- `GenericCsvExporter` em `infrastructure.utils`
- Usa Reflection (`Method`, `Field`, `Class`)
- ConsoleController chama UseCase para obter dados
- ConsoleController passa dados para GenericCsvExporter

## 🏗 Arquitetura - Clean Architecture

A aplicação segue rigorosamente os princípios da Clean Architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                     Infrastructure                           │
│  (Interface Adapters - Detalhes de Implementação)           │
│                                                              │
│  - persistence/  (Repositórios em memória com Map/Queue)    │
│  - ui/           (ConsoleController, ConsoleView)           │
│  - utils/        (GenericCsvExporter - Reflection)          │
└──────────────────────┬──────────────────────────────────────┘
                       │ Depende de
┌──────────────────────▼──────────────────────────────────────┐
│                     Application                              │
│  (Use Cases - Casos de Uso da Aplicação)                    │
│                                                              │
│  - usecases/     (13 Use Cases implementados)               │
│  - repositories/ (Interfaces - Abstrações)                  │
└──────────────────────┬──────────────────────────────────────┘
                       │ Depende de
┌──────────────────────▼──────────────────────────────────────┐
│                      Domain                                  │
│  (Entidades e Regras de Negócio - CORE)                     │
│                                                              │
│  - entities/     (Course, User, Student, etc.)              │
│  - enums/        (DifficultyLevel, CourseStatus)            │
│  - exceptions/   (BusinessException, EnrollmentException)   │
└─────────────────────────────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                       Main                                   │
│  (Frameworks & Drivers - Composição e Injeção)              │
│                                                              │
│  - Main.java      (Injeção de Dependência Manual)           │
│  - InitialData.java (Popular dados iniciais)                │
└─────────────────────────────────────────────────────────────┘
```

### Regra da Dependência

**As dependências apontam sempre para dentro!**

- `infrastructure` depende de `application` e `domain`
- `application` depende apenas de `domain`
- `domain` **NÃO depende de NINGUÉM** ✅

### Componentes Principais

- **Domain**: Entidades puras com lógica de negócio (ex: `student.canEnroll()`)
- **Application**: UseCases que orquestram entidades através de interfaces
- **Infrastructure**: Implementações concretas (persistência em memória, UI, CSV)
- **Main**: Composição e injeção manual de todas as dependências

## 🛠 Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Maven** - Gerenciamento de dependências
- **Collections Java** - Map, Set, Queue, List para persistência em memória
- **Stream API (Java 8+)** - Programação funcional para relatórios
- **Reflection** - Exportação CSV genérica

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

### Executando a Aplicação

```bash
# Navegue até o diretório do projeto
cd AcademiaDev

# Compile o projeto
mvn clean compile

# Execute a aplicação
mvn exec:java -Dexec.mainClass="com.academiadev.main.Main"
```

### Usuários Pré-cadastrados

**Administrador:**
- Email: `admin@academiadev.com`

**Alunos:**
- Email: `maria@email.com` (Plano: Basic)
- Email: `pedro@email.com` (Plano: Premium)
- Email: `ana@email.com` (Plano: Basic)
- Email: `carlos@email.com` (Plano: Premium)

## 📡 Funcionalidades da Aplicação

### Operações de Administrador (Admin)

| Funcionalidade | Descrição |
|----------------|-----------|
| Gerenciar Status de Cursos | Ativar/inativar cursos existentes |
| Gerenciar Planos de Alunos | Alterar o plano de assinatura de um aluno |
| Atender Tickets de Suporte | Processar tickets da fila em ordem FIFO |
| Gerar Relatórios e Análises | Acessar todos os relatórios da plataforma |
| Exportar Dados | Gerar CSV com colunas selecionáveis dinamicamente |

### Operações do Aluno (Student)

| Funcionalidade | Descrição |
|----------------|-----------|
| Matricular-se em Curso | Matricular desde que o plano permita e curso esteja ACTIVE |
| Consultar Matrículas | Ver todos os cursos matriculados e progresso |
| Atualizar Progresso | Modificar percentual de conclusão de um curso (0-100%) |
| Cancelar Matrícula | Remover-se de um curso (libera vaga para planos básicos) |

### Operações Gerais (Qualquer Usuário)

| Funcionalidade | Descrição |
|----------------|-----------|
| Consultar Catálogo de Cursos | Listar cursos ativos disponíveis |
| Abrir Ticket de Suporte | Criar um novo ticket para a fila de atendimento |
| Autenticação Simples | Login por email (sem senha) |

## 📝 Exemplos de Uso

### Menu do Administrador

```
╔════════════════════════════════════════════════════════════╗
║     Bem-vindo à AcademiaDev - Plataforma de Cursos         ║
╚════════════════════════════════════════════════════════════╝

Autenticado como Administrador: João Admin

═══════════════════════════════════════════════════════════════
                      MENU PRINCIPAL                           
═══════════════════════════════════════════════════════════════

[1] Consultar Catálogo de Cursos
[2] Abrir Ticket de Suporte

--- OPERAÇÕES DE ADMINISTRADOR ---
[3] Gerenciar Status de Cursos
[4] Gerenciar Planos de Alunos
[5] Atender Tickets de Suporte
[6] Gerar Relatórios e Análises
[7] Exportar Dados para CSV

[0] Sair
```

### Menu do Aluno

```
Autenticado como Aluno: Maria Silva

═══════════════════════════════════════════════════════════════
                      MENU PRINCIPAL                           
═══════════════════════════════════════════════════════════════

[1] Consultar Catálogo de Cursos
[2] Abrir Ticket de Suporte

--- OPERAÇÕES DO ALUNO ---
[3] Matricular-se em Curso
[4] Consultar Minhas Matrículas
[5] Atualizar Progresso
[6] Cancelar Matrícula

[0] Sair
```

### Relatórios Disponíveis

```
═══════════════════════════════════════════════════════════════
                  RELATÓRIOS E ANÁLISES                        
═══════════════════════════════════════════════════════════════
[1] Cursos por Nível de Dificuldade
[2] Instrutores Únicos de Cursos Ativos
[3] Alunos Agrupados por Plano
[4] Média Geral de Progresso
[5] Aluno com Mais Matrículas Ativas
[0] Voltar
```

## ✅ Validações Implementadas

### Validações de Negócio (Domain e UseCases)

**Regras implementadas:**
- Título de curso deve ser único
- Email de usuário deve ser único
- Aluno só pode se matricular se plano permitir (BasicPlan: máximo 3 ativas)
- Aluno só pode se matricular em curso com status ACTIVE
- Aluno não pode se matricular duas vezes no mesmo curso
- Progresso deve estar entre 0 e 100%
- Data limite não pode ser no passado (validação implementada)

**Exceções customizadas:**
- `EnrollmentException`: Para erros relacionados a matrículas
- `BusinessException`: Para erros gerais de negócio

## 🔒 Tratamento de Exceções

Exceções são tratadas na camada UI (infrastructure.ui):

**Exemplo de tratamento:**
```java
// UseCase lança exceção
throw new EnrollmentException("Curso não encontrado: " + courseTitle);

// Controller captura e exibe mensagem amigável
catch (EnrollmentException e) {
    view.showError(e.getMessage());
}
```

**Tipos de exceções:**
- `EnrollmentException`: Erros relacionados a matrículas
- `BusinessException`: Erros gerais de negócio

## 📊 Diagrama de Classes UML

Para visualizar o diagrama completo de classes UML do sistema AcademiaDev, consulte o arquivo [`DIAGRAMA_UML.md`](./DIAGRAMA_UML.md) na raiz do projeto.

O diagrama representa:
- **Domain Layer**: Entidades, Enums e Exceções (sem dependências externas)
- **Application Layer**: Use Cases e Interfaces de Repositório (depende apenas de Domain)
- **Infrastructure Layer**: Implementações concretas (depende de Application e Domain)
- **Main Layer**: Composição e injeção manual

**Relacionamentos principais:**
- Herança: `Student extends User`, `BasicPlan extends SubscriptionPlan`
- Implementação: Repositórios em memória implementam interfaces de Application
- Composição: UseCases recebem repositórios via construtor
- Agregação: `Enrollment` contém `Student` e `Course`

## 🔍 Justificativa de Design

Esta seção explica detalhadamente como a **Regra da Dependência** da Clean Architecture foi seguida rigorosamente no projeto AcademiaDev e como os detalhes de implementação (CSV com Reflection e persistência em memória) foram isolados na camada Infrastructure.

### 1. Como a Regra da Dependência foi Seguida

A **Regra da Dependência** estabelece que **as dependências sempre apontam para dentro**, ou seja, as camadas mais externas dependem das camadas mais internas, nunca o contrário.

#### 1.1. Camada Domain - Mantida Pura

**Requisito:** Classes do domain não podem ter import de nada das camadas application ou infrastructure.

**Implementação:**

Verificação realizada em todas as classes do domain:
```bash
# Nenhum arquivo no domain importa application ou infrastructure
✅ Domain está puro - sem imports de outras camadas
```

**Exemplo prático - Student.java:**
```java
// domain/entities/Student.java
package com.academiadev.domain.entities;

// ✅ Apenas imports do próprio domain ou bibliotecas padrão do Java
// ❌ NENHUM import de com.academiadev.application
// ❌ NENHUM import de com.academiadev.infrastructure

public class Student extends User {
    private SubscriptionPlan subscriptionPlan;
    
    // Lógica de negócio pura no domain
    public boolean canEnroll(int currentActiveEnrollments) {
        return subscriptionPlan.canEnroll(currentActiveEnrollments);
    }
}
```

**Verificação de conformidade:**
- ✅ Nenhuma classe do domain importa `com.academiadev.application.*`
- ✅ Nenhuma classe do domain importa `com.academiadev.infrastructure.*`
- ✅ Domain importa apenas: `domain.enums`, `domain.entities`, bibliotecas padrão do Java

**Benefícios:**
- Lógica de negócio completamente independente de detalhes de implementação
- Pode ser testada sem frameworks ou bibliotecas externas
- Pode ser reutilizada em diferentes contextos (CLI, Web, Mobile, etc.)

#### 1.2. Camada Application - Depende apenas de Domain

**Requisito:** Application depende do domain, mas NUNCA do infrastructure.

**Implementação:**

UseCases dependem apenas de:
- Interfaces de repositório (`application.repositories`) - **Abstrações, não implementações**
- Entidades do domain (`domain.entities`, `domain.enums`, `domain.exceptions`)

**Exemplo prático - MatricularAlunoUseCase.java:**
```java
// application/usecases/MatricularAlunoUseCase.java
package com.academiadev.application.usecases;

// ✅ Importa interfaces (abstrações) de application.repositories
import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;

// ✅ Importa entidades do domain
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.exceptions.EnrollmentException;

// ❌ NÃO importa com.academiadev.infrastructure.*
// ❌ NÃO importa implementações concretas

public class MatricularAlunoUseCase {
    // Recebe INTERFACES por injeção de dependência (via construtor)
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    
    public MatricularAlunoUseCase(EnrollmentRepository enrollmentRepository, 
                                   CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }
    
    // Lógica de negócio usando as interfaces
    public Enrollment execute(Student student, String courseTitle) {
        // ... implementação
    }
}
```

**Verificação de conformidade:**
- ✅ UseCases importam apenas interfaces (`application.repositories.*`)
- ✅ UseCases importam entidades do domain
- ✅ UseCases recebem repositórios via construtor (injeção de dependência)
- ✅ Nenhum UseCase importa `com.academiadev.infrastructure.*`

**Benefícios:**
- UseCases não conhecem detalhes de implementação (Map, ArrayDeque, etc.)
- Facilmente testável com mocks das interfaces
- Pode trocar implementações sem alterar UseCases

#### 1.3. Camada Infrastructure - Implementa os Detalhes

**Requisito:** Infrastructure depende de application e domain, implementa os detalhes de frameworks.

**Implementação:**

Infrastructure contém:
- **Implementações concretas** das interfaces de `application.repositories`
- **UI** (ConsoleController, ConsoleView)
- **Utilitários** com detalhes de framework (GenericCsvExporter com Reflection)

**Exemplo prático - CourseRepositoryEmMemoria.java:**
```java
// infrastructure/persistence/CourseRepositoryEmMemoria.java
package com.academiadev.infrastructure.persistence;

// ✅ Implementa interface de application
import com.academiadev.application.repositories.CourseRepository;

// ✅ Importa entidades do domain
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;

// ✅ Usa Collections Java (detalhe de implementação)
import java.util.*;
import java.util.stream.Collectors;

// ✅ IMPLEMENTAÇÃO CONCRETA da interface
public class CourseRepositoryEmMemoria implements CourseRepository {
    // ✅ Detalhe de implementação: Map para garantir unicidade
    private final Map<String, Course> courses;
    
    public CourseRepositoryEmMemoria() {
        this.courses = new HashMap<>(); // HashMap é um detalhe!
    }
    
    @Override
    public void save(Course course) {
        courses.put(course.getTitle(), course); // Map.put() é um detalhe!
    }
    
    // ... outros métodos
}
```

**Verificação de conformidade:**
- ✅ Infrastructure implementa interfaces de `application.repositories`
- ✅ Infrastructure usa detalhes de implementação (Map, Queue, Reflection)
- ✅ Infrastructure conhece domain para acessar entidades
- ✅ Infrastructure conhece application para implementar interfaces

**Benefícios:**
- Detalhes isolados em uma única camada
- Facilmente substituível (ex: trocar Map por PostgreSQL)
- Não afeta outras camadas ao alterar implementação

#### 1.4. Camada Main - Composição e Injeção Manual

**Requisito:** Main.java é o único lugar que conhece todas as camadas.

**Implementação:**

O `Main.java` faz a **composição explícita** de todas as dependências:

```java
// main/Main.java
package com.academiadev.main;

// ✅ Main conhece TODAS as camadas
import com.academiadev.application.repositories.*;        // Interfaces
import com.academiadev.application.usecases.*;            // UseCases
import com.academiadev.infrastructure.persistence.*;      // Implementações
import com.academiadev.infrastructure.ui.*;               // UI

public class Main {
    public static void main(String[] args) {
        // 1. Criar implementações concretas (infrastructure)
        CourseRepository courseRepository = new CourseRepositoryEmMemoria();
        UserRepository userRepository = new UserRepositoryEmMemoria();
        EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryEmMemoria();
        SupportTicketQueue ticketQueue = new SupportTicketQueueEmMemoria();
        
        // 2. Criar UseCases (application) - recebem INTERFACES
        MatricularAlunoUseCase matricularAlunoUseCase = new MatricularAlunoUseCase(
            enrollmentRepository, courseRepository); // Injeta interfaces!
        
        // 3. Criar UI (infrastructure)
        ConsoleView view = new ConsoleView();
        ConsoleController controller = new ConsoleController(
            view, userRepository, courseRepository, enrollmentRepository,
            matricularAlunoUseCase, // Injeta UseCases!
            // ... outros use cases
        );
        
        // 4. Popular dados iniciais
        InitialData.populate(userRepository, courseRepository);
        
        // 5. Iniciar aplicação
        controller.start();
    }
}
```

**Verificação de conformidade:**
- ✅ Main.java é o único arquivo que importa todas as camadas
- ✅ Main.java cria todas as implementações concretas
- ✅ Main.java injeta interfaces nos UseCases
- ✅ Injeção de dependência é manual (sem framework)

**Benefícios:**
- Composição explícita e clara
- Fácil de entender e depurar
- Facilmente testável (pode injetar mocks facilmente)

### 2. Como os Detalhes foram Isolados na Infrastructure

#### 2.1. Isolamento da Persistência em Memória

**Persistência como detalhe de implementação:**

A persistência em memória usando `Map` e `Queue` está completamente isolada na camada `infrastructure.persistence`.

**Estruturas de dados utilizadas:**

1. **Map<String, Course>** - CourseRepositoryEmMemoria
   ```java
   // infrastructure/persistence/CourseRepositoryEmMemoria.java
   private final Map<String, Course> courses = new HashMap<>();
   
   // Garante unicidade de título (chave do Map)
   public void save(Course course) {
       courses.put(course.getTitle(), course);
   }
   ```

2. **Map<String, User>** - UserRepositoryEmMemoria
   ```java
   // infrastructure/persistence/UserRepositoryEmMemoria.java
   private final Map<String, User> users = new HashMap<>();
   
   // Garante unicidade de email (chave do Map)
   public void save(User user) {
       users.put(user.getEmail(), user);
   }
   ```

3. **ArrayDeque<SupportTicket>** - SupportTicketQueueEmMemoria
   ```java
   // infrastructure/persistence/SupportTicketQueueEmMemoria.java
   private final Queue<SupportTicket> tickets = new ArrayDeque<>();
   
   // Garante comportamento FIFO
   public void addTicket(SupportTicket ticket) {
       tickets.offer(ticket); // Adiciona no final
   }
   
   public Optional<SupportTicket> nextTicket() {
       return Optional.ofNullable(tickets.poll()); // Remove do início (FIFO)
   }
   ```

**Por que está isolado na Infrastructure?**

- `Map` e `ArrayDeque` são detalhes de implementação do Java Collections
- Se quisermos migrar para banco de dados (PostgreSQL, MongoDB, etc.), apenas criamos novas implementações:
  ```java
  public class CourseRepositoryPostgreSQL implements CourseRepository {
      // Usa JDBC, Hibernate, etc. - outro detalhe!
  }
  ```
- A camada `application` e `domain` não precisam ser alteradas

**Exemplo de migração (hipotético):**
```java
// Em Main.java, apenas trocar:
// ANTES (memória):
CourseRepository courseRepository = new CourseRepositoryEmMemoria();

// DEPOIS (PostgreSQL):
CourseRepository courseRepository = new CourseRepositoryPostgreSQL();

// ✅ UseCases não precisam ser alterados!
// ✅ Domain não precisa ser alterado!
```

#### 2.2. Isolamento do CSV Exporter com Reflection

**Reflection como detalhe de framework:**

A classe `GenericCsvExporter` que usa Reflection está isolada em `infrastructure.utils`.

**Localização:**
```
infrastructure/utils/GenericCsvExporter.java
```

**Por que está na Infrastructure?**

1. **Reflection é um detalhe de framework:**
   - Reflection é uma API específica do Java
   - A lógica de negócio não precisa saber como funciona Reflection
   - Se quisermos trocar por outra biblioteca (ex: Jackson), apenas alteramos `infrastructure.utils`

2. **Application não conhece GenericCsvExporter:**
   ```java
   // ✅ CORRETO: ConsoleController (infrastructure) conhece ambos
   // infrastructure/ui/ConsoleController.java
   import com.academiadev.application.usecases.*;        // UseCase
   import com.academiadev.infrastructure.utils.GenericCsvExporter; // Reflection
   
   private void exportToCsv(Scanner scanner) {
       // 1. Chama UseCase para obter dados (sem Reflection)
       List<Course> courses = courseRepository.findAll();
       
       // 2. Passa dados para GenericCsvExporter (com Reflection)
       String csv = GenericCsvExporter.exportToCsv(courses, fields);
       
       view.showCsvExport(csv);
   }
   
   // ❌ ERRADO: UseCase (application) NÃO conhece GenericCsvExporter
   // application/usecases/MatricularAlunoUseCase.java
   // ❌ import com.academiadev.infrastructure.utils.GenericCsvExporter; // NÃO!
   ```

3. **Fluxo correto de exportação CSV:**
   ```
   ConsoleController (infrastructure)
       ↓ chama
   Repository ou UseCase (application)
       ↓ retorna
   List<Course> ou List<Student>
       ↓ passa para
   GenericCsvExporter.exportToCsv() (infrastructure.utils)
       ↓ usa Reflection
   String CSV formatada
   ```

**Exemplo prático do isolamento:**
```java
// infrastructure/utils/GenericCsvExporter.java
package com.academiadev.infrastructure.utils; // ✅ Na infrastructure!

import java.lang.reflect.Field;    // ✅ Reflection (detalhe de framework)
import java.lang.reflect.Method;   // ✅ Reflection (detalhe de framework)
import java.lang.reflect.Class;    // ✅ Reflection (detalhe de framework)

public class GenericCsvExporter {
    // ✅ Usa Reflection - detalhe de implementação isolado aqui
    public static <T> String exportToCsv(List<T> data, List<String> selectedFields) {
        Class<?> clazz = data.get(0).getClass(); // Reflection!
        Method method = clazz.getMethod(...);     // Reflection!
        // ...
    }
}
```

**Benefícios do isolamento:**
- Se quisermos trocar Reflection por Jackson, apenas alteramos `infrastructure.utils`
- Se quisermos exportar JSON ao invés de CSV, criamos `GenericJsonExporter` na mesma camada
- Application e Domain não precisam saber como funciona a exportação

### 3. Fluxo de Dependências - Direção das Setas

**Regra:** As dependências sempre apontam para dentro.

**Visualização:**

```
┌─────────────────────────────────────┐
│      Infrastructure (mais externa)  │
│  ← Setas apontam para dentro        │
│  ← Depende de Application e Domain  │
└──────────────┬──────────────────────┘
               │ Dependências
┌──────────────▼──────────────────────┐
│      Application (intermediária)    │
│  ← Setas apontam para dentro        │
│  ← Depende apenas de Domain         │
└──────────────┬──────────────────────┘
               │ Dependências
┌──────────────▼──────────────────────┐
│      Domain (mais interna)          │
│  ← NÃO depende de NINGUÉM           │
│  ← Setas não apontam para fora      │
└─────────────────────────────────────┘
```

**Exemplo prático de imports:**

```java
// ❌ Domain NÃO pode importar:
// domain/entities/Student.java
import com.academiadev.application.*;      // ❌ ERRO!
import com.academiadev.infrastructure.*;   // ❌ ERRO!

// ✅ Application pode importar:
// application/usecases/MatricularAlunoUseCase.java
import com.academiadev.domain.*;           // ✅ OK!
import com.academiadev.application.repositories.*; // ✅ OK! (interfaces)
// ❌ import com.academiadev.infrastructure.*;      // ❌ ERRO!

// ✅ Infrastructure pode importar:
// infrastructure/persistence/CourseRepositoryEmMemoria.java
import com.academiadev.domain.*;           // ✅ OK!
import com.academiadev.application.repositories.*; // ✅ OK! (implementa interface)
import com.academiadev.infrastructure.*;   // ✅ OK! (própria camada)
```

### 4. Resumo da Justificativa

**Regra da Dependência seguida:**
- ✅ Domain não depende de ninguém
- ✅ Application depende apenas de Domain
- ✅ Infrastructure depende de Application e Domain
- ✅ Main conhece todas as camadas (composição)

**Detalhes isolados na Infrastructure:**
- ✅ Persistência em memória (Map, Queue) em `infrastructure.persistence`
- ✅ Reflection (CSV Export) em `infrastructure.utils`
- ✅ UI (Console) em `infrastructure.ui`
- ✅ Domain e Application não conhecem esses detalhes

**Benefícios alcançados:**
- ✅ Testabilidade: Lógica de negócio testável sem frameworks
- ✅ Flexibilidade: Fácil trocar implementações (banco, UI, exportação)
- ✅ Manutenibilidade: Alterações em detalhes não afetam lógica de negócio
- ✅ Independência: Domain e Application não dependem de frameworks externos

## 🧪 Testes

A arquitetura permite testar facilmente cada camada isoladamente:

```java
// Teste de Use Case com mock
@Test
void testMatricularAluno() {
    CourseRepository courseRepo = mock(CourseRepository.class);
    EnrollmentRepository enrollmentRepo = mock(EnrollmentRepository.class);
    
    MatricularAlunoUseCase useCase = new MatricularAlunoUseCase(
        enrollmentRepo, courseRepo);
    
    // Testar lógica de negócio isoladamente
}
```

## 🔍 Conceitos Aprendidos

### Clean Architecture

- **Domain**: Camada mais interna, sem dependências externas
- **Application**: Orquestra entidades através de UseCases
- **Infrastructure**: Implementa detalhes (persistência, UI, frameworks)
- **Main**: Composição e injeção manual de dependências
- **Regra da Dependência**: Dependências sempre apontam para dentro

### Injeção de Dependência Manual

- **Main.java** é o único lugar que conhece todas as camadas
- Cria implementações concretas (infrastructure)
- Injeta interfaces nos UseCases (application)
- Composição explícita e testável

### Isolamento de Detalhes

- **Persistência em memória**: Map para unicidade, Queue para FIFO
- **Reflection**: Isolado em infrastructure.utils
- **UI**: ConsoleController e ConsoleView isolados
- **Domain não conhece detalhes**: Lógica de negócio pura

### Programação Funcional

- **Streams nos UseCases**: Relatórios implementados com Stream API
- **Operações funcionais**: filter, map, collect, groupingBy, average
- **Código declarativo**: Mais expressivo e legível

### Estruturas de Dados

- **Map**: Para garantir unicidade (Courses por title, Users por email)
- **Set**: Para coleções sem duplicatas (instrutores únicos)
- **Queue (ArrayDeque)**: Para comportamento FIFO (tickets de suporte)
- **List**: Para coleções ordenadas

## 🏛 Princípios e Boas Práticas Aplicadas

1. ✅ **Clean Architecture**: Separação rigorosa de camadas
2. ✅ **Domain Puro**: Sem dependências de outras camadas
3. ✅ **Injeção de Dependência**: Manual no Main.java
4. ✅ **Interfaces na Application**: Abstrações independentes de implementação
5. ✅ **Detalhes Isolados**: Persistência, UI e Reflection na infrastructure
6. ✅ **Lógica de Negócio no Domain**: Entidades com métodos de negócio
7. ✅ **UseCases**: Cada caso de uso em uma classe dedicada
8. ✅ **Programação Funcional**: Streams para relatórios
9. ✅ **Reflection Isolado**: GenericCsvExporter na infrastructure
10. ✅ **Tratamento de Exceções**: Exceções customizadas no domain

## 📖 Referências

- [Clean Architecture - Robert C. Martin (Uncle Bob)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Clean Architecture - Otávio Lemos](https://www.youtube.com/watch?v=1VdX6hWm2yQ)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Java Streams API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Java Reflection API](https://docs.oracle.com/javase/tutorial/reflect/)

## 👤 Autor

Ana Layslla - https://www.linkedin.com/in/ana-layslla/ & Beatriz Mazzucatto - www.linkedin.com/in/beatriz-mazzucatto-seabra

---

**Instituto Federal de Educação, Ciência e Tecnologia de São Paulo, Câmpus Guarulhos.**  
**APIs e Microsserviços - Prof. Giovani.**
