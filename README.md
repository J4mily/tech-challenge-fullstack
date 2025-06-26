# Tech Challenge - Fullstack

Este repositório contém o código-fonte do backend para o **Desafio Técnico Fullstack do Instituto Senai de Inovação**.  
A aplicação simula o núcleo de um sistema de e-commerce, com foco no cadastro, gestão e aplicação de descontos em produtos, seguindo rigorosas regras de negócio.

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído com as seguintes tecnologias e boas práticas:

### Backend:
- **Linguagem:** Java 17  
- **Framework:** Spring Boot 3  
- **Persistência de Dados:** Spring Data JPA com Hibernate  
- **Banco de Dados:** PostgreSQL
- **Build Tool:** Maven  
- **Validações:** Spring Validation (Bean Validation)  
- **Documentação da API:** SpringDoc OpenAPI (Swagger UI)  
- **Testes:** JUnit 5 e Mockito  
- **Utilitários:** Lombok

### Frontend:
- **Linguagem:** TypeScript  
- **Framework:** Next  
- **Build Tool:** Maven  
- **Bibliotecas de UI e estilização:** TailwindCSS  
- **Bibliotecas auxiliares:** Axios

---

## 📦 Deploy

Foi utilizado **Docker Compose** para gerenciar as aplicações.

---

## 🏗️ Arquitetura e Decisões de Projeto

O projeto adota uma **arquitetura monolítica modular**, escolhida para maximizar a agilidade e simplicidade no contexto do desafio.  
Todas as funcionalidades residem em uma única base de código, facilitando o desenvolvimento, testes e implantação, mantendo forte consistência transacional.

Para garantir um código limpo e de fácil manutenção, foram aplicados os seguintes padrões de projeto:

### Backend:
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

### Frontend:
- **Component-Based Architecture:**  
  Base do React. Quebramos a interface em peças pequenas e reutilizáveis (`Sidebar`, `Header`, `ProductTable`, `Modal`), tornando o código mais organizado, legível e fácil de manter.

- **Container/Presentational Pattern (Smart/Dumb Components):**  
  Separamos os componentes em dois tipos. A página `HomePage` é o componente "inteligente" (Container), que detém a lógica e o estado.  
  Componentes como `ProductTable` e `ProductForm` são "burros" (Presentational), pois apenas recebem dados via props e emitem eventos.

- **Service Layer Pattern:**  
  Toda a comunicação com o backend está isolada em `services/api.ts`. Os componentes não fazem chamadas Axios diretamente; utilizam funções como `getProducts()`.  
  Se a API mudar, basta alterar esse único arquivo.

- **Custom Hooks Pattern:**  
  Lógicas complexas e reutilizáveis foram encapsuladas em *custom hooks*. O `useDebounce` é um bom exemplo, otimizando chamadas à API durante a busca sem poluir os componentes.

- **Type Safety (Segurança de Tipos):**  
  O TypeScript define a "forma" dos dados (`types/index.ts`) e das props dos componentes. Isso previne bugs e ativa autocompletar no editor, servindo como um "copiloto" durante o desenvolvimento.

---
## 🏗️ Estrutura do Projeto

### 📦 Backend

O backend segue uma estrutura organizada em pacotes, separando as responsabilidades de cada camada da aplicação.

- `/config`  
  Contém classes de configuração do Spring, como o `WebConfig` para ajustes de CORS.

- `/controller`  
  Responsável por expor a API REST. Recebe as requisições HTTP, aciona a camada de serviço e retorna as respostas ao cliente.

- `/dto` (Data Transfer Object)  
  Define os objetos que carregam dados entre as camadas e formam o contrato da API (requests e responses), desacoplando a visão externa da estrutura interna do banco de dados.

- `/exception`  
  Centraliza as exceções customizadas da aplicação, permitindo um tratamento de erros mais claro e específico para cada cenário de negócio.

- `/model`  
  Contém as entidades JPA que mapeiam as tabelas do banco de dados (ex: `Product`, `Coupon`). É a representação do domínio da aplicação.

- `/repository`  
  Interfaces do Spring Data JPA que abstraem o acesso aos dados, fornecendo métodos para operações de CRUD e queries customizadas sem a necessidade de SQL explícito.

- `/service`  
  Onde reside a lógica de negócio. Orquestra as operações, utilizando os repositórios para acessar os dados e aplicando as regras de negócio antes de entregar os resultados aos controllers.

- `/test`  
  Armazena os testes unitários e de integração, garantindo a qualidade, a confiabilidade e o correto funcionamento das regras de negócio e dos endpoints da API.

---

### 💻 Frontend

O frontend é estruturado com base no ecossistema do **Next.js**, priorizando a componentização e a separação de responsabilidades.

- `/src/app`  
  Núcleo do roteamento e das páginas da aplicação. Contém o layout global (`layout.tsx`) e a página principal (`page.tsx`).

- `/src/components`  
  Armazena todos os componentes React reutilizáveis que formam a interface do usuário, como botões, modais, tabelas e formulários.

- `/src/hooks`  
  Contém os custom hooks do React, que encapsulam lógicas reutilizáveis e com estado, como `useDebounce` para otimizar buscas.

- `/src/service`  
  Isola a comunicação com a API backend. O arquivo `api.ts` centraliza todas as chamadas HTTP (Axios), facilitando a manutenção.

- `/src/types`  
  Define as interfaces e tipos do TypeScript usados em todo o projeto, garantindo a segurança de tipos para props, estados e dados da API.
---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos:
- Ter o Docker instalado  
- Configurar o arquivo `.env`

### Execução:

Clone o repositório:
```bash
git clone https://github.com/J4mily/tech-challenge-fullstack.git
cd tech-challenge-fullstack/backend
```

Entre na pasta do projeto, crie o `.env` e execute:
```bash
docker-compose up
```

### Links auxiliares:
- **Documentação Swagger:** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)  
- **Aplicação:** [http://localhost:3000/#](http://localhost:3000/#)  
- **Banco de dados:** `jdbc:postgresql://postgres:5432/techchallenge_prod`  
  > Obs: A senha e o usuário estão presentes no arquivo `.env.example`.

---

## 🚧 Implementações Ausentes

- Lógica do `one_shot`, `max_uses` e `uses_count` no fluxo de aplicação de cupons.  
- Adequar o PATCH ao padrão proposto no trecho do desafio (`2.1.3 PATCH /products/{id}`).  
- Lógica para trazer produtos inativos (`includeDeleted`), assim como o checkbox na tela de edição do item.

> ⚠️ Esses pontos seriam ajustados com mais calma, caso fosse possível estender o tempo do projeto.  
Além disso, existem outros pontos que gostaria de revisar e aprimorar conforme os objetivos do desafio.

---

## 🌱 Melhorias Futuras

- Adicionar um *exception handler global*  
- Melhorar a documentação Swagger (deixada simples devido ao prazo)  
- Implementar lógica de usuários (Spring Security + JWT)  
- Melhorar a estrutura de componentização  
- Tornar a aplicação responsiva para diferentes dispositivos  
- Utilizar SVGs no lugar de bibliotecas para melhorar a performance

> 💡 Como evolução futura, está nos planos a refatoração desta aplicação para uma arquitetura baseada em **microsserviços**.

