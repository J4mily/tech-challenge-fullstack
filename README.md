# Tech Challenge - Fullstack (Backend)

Este repositório contém o código-fonte do backend para o **Desafio Técnico Fullstack do Instituto Senai de Inovação**.  
A aplicação simula o núcleo de um sistema de e-commerce, com foco no cadastro, gestão e aplicação de descontos em produtos, seguindo rigorosas regras de negócio.

---

## 🚀 Tecnologias Utilizadas

O backend foi construído com as seguintes tecnologias e boas práticas:

- **Linguagem:** Java 17  
- **Framework:** Spring Boot 3  
- **Persistência de Dados:** Spring Data JPA com Hibernate  
- **Banco de Dados:** H2 (relacional em memória)  
- **Build Tool:** Maven  
- **Validações:** Spring Validation (Bean Validation)  
- **Documentação da API:** SpringDoc OpenAPI (Swagger UI)  
- **Testes:** JUnit 5 e Mockito  
- **Utilitários:** Lombok  

---

## 🏗️ Arquitetura e Decisões de Projeto

O projeto adota uma **arquitetura monolítica modular**, escolhida para maximizar a agilidade e simplicidade no contexto do desafio.  
Todas as funcionalidades residem em uma única base de código, facilitando o desenvolvimento, os testes e a implantação, mantendo forte consistência transacional.

Para garantir um código limpo e de fácil manutenção, foram aplicados os seguintes padrões de projeto:

- **Repository Pattern:**  
  Abstração do acesso aos dados (`ProductRepository`, `CouponRepository`), tornando a lógica de negócio independente do banco.

- **Service Layer Pattern:**  
  Centraliza a lógica de negócio (`ProductServiceImpl`), criando um núcleo reutilizável e testável.

- **DTO Pattern (Data Transfer Object):**  
  Define um contrato seguro para a API (`ProductRequestDTO`, `ProductResponseDTO`), separando as camadas de dados internas e externas.

- **Dependency Injection (DI) & Inversion of Control (IoC):**  
  Utiliza o Spring para gerenciar e injetar dependências com anotações como `@RequiredArgsConstructor`, deixando o código mais desacoplado.

- **Specification Pattern:**  
  Permite construir queries de busca complexas de forma programática e segura, evitando SQL manual.

> 💡 Como evolução futura, está nos planos a refatoração desta aplicação para uma arquitetura baseada em microsserviços.

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos

- JDK 17 ou superior  
- Apache Maven 3.6 ou superior  

### Execução

Clone o repositório:

```bash
git clone https://github.com/J4mily/tech-challenge-fullstack.git
cd tech-challenge-fullstack/backend
