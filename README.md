# Tech Challenge - Fullstack (Backend)

Este repositório contém o código-fonte do backend para o **Desafio Técnico Fullstack do Instituto Senai de Inovação**.  
A aplicação simula o núcleo de um sistema de e-commerce, com foco no cadastro, gestão e aplicação de descontos em produtos, seguindo rigorosas regras de negócio.

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído com as seguintes tecnologias e boas práticas:

## Backend:

- **Linguagem:** Java 17  
- **Framework:** Spring Boot 3  
- **Persistência de Dados:** Spring Data JPA com Hibernate  
- **Banco de Dados:** H2 (relacional em memória)  
- **Build Tool:** Maven  
- **Validações:** Spring Validation (Bean Validation)  
- **Documentação da API:** SpringDoc OpenAPI (Swagger UI)  
- **Testes:** JUnit 5 e Mockito  
- **Utilitários:** Lombok

## Frontend:
- **Linguagem:** Typescript  
- **Framework:** Next  
- **Build Tool:** Maven  
- **bibliotecas de UI e estilização:** TailwindCSS
- **Bibliotecas auxiliares:** Axios

## Deploy
Foi utilizado Docker compose para gerenciar as aplicações. 

---

## 🏗️ Arquitetura e Decisões de Projeto

O projeto adota uma **arquitetura monolítica modular**, escolhida para maximizar a agilidade e simplicidade no contexto do desafio.  
Todas as funcionalidades residem em uma única base de código, facilitando o desenvolvimento, os testes e a implantação, mantendo forte consistência transacional.
Para garantir um código limpo e de fácil manutenção, foram aplicados os seguintes padrões de projeto:

## Backend: 

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


## Frontend: 
- **Component-Based Architecture:**
É a base do React. Quebrámos a nossa interface em peças pequenas e reutilizáveis (Sidebar, Header, ProductTable, Modal), o que torna o código mais organizado, legível e fácil de manter.

- **Container/Presentational Pattern (Smart/Dumb Components):**
Separamos os nossos componentes em dois tipos. A nossa página HomePage é o componente "inteligente" (Container) que detém a lógica e o estado. Componentes como ProductTable e ProductForm são "burros" (Presentational), pois apenas recebem dados via props e emitem eventos, sem saberem da lógica de negócio.

- **Service Layer Pattern:**
Isolámos toda a comunicação com o backend no ficheiro services/api.ts. Os nossos componentes não fazem chamadas axios diretamente; eles chamam funções como getProducts(). Se a API mudar, só precisamos de alterar este único ficheiro.

- **Custom Hooks Pattern:**
Encapsulámos lógicas complexas e reutilizáveis em "hooks" customizados. O nosso useDebounce é um exemplo perfeito, permitindo-nos otimizar as chamadas à API durante a busca sem poluir os nossos componentes com código setTimeout.

- **Type Safety (Segurança de Tipos):**
Usámos o TypeScript para definir a "forma" dos nossos dados (types/index.ts) e das props dos nossos componentes. Isto previne uma vasta classe de bugs e ativa o autocompletar inteligente no editor de código, agindo como um "copiloto" durante o desenvolvimento.

> 💡 Como evolução futura, está nos planos a refatoração desta aplicação para uma arquitetura baseada em microsserviços.

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos

- Ter o docker instalado
- Configurar o .env

### Execução

Clone o repositório:

```bash
git clone https://github.com/J4mily/tech-challenge-fullstack.git
cd tech-challenge-fullstack/backend
```
Entre na pasta do projeto e crie o .env, após isso execute o comando:

```bash
docker-compose up
```

