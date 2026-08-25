# 🏥 Sistema de Consultório

Sistema **full stack para gerenciamento de um consultório médico**, desenvolvido com React no frontend e Java + Spring Boot no backend.

O projeto simula um sistema real de consultório, permitindo o gerenciamento de pacientes, médicos, especialidades, agendas e consultas.

## 🚀 Aplicação Online

### 🌐 Frontend

**[Acessar o sistema](https://consultorio-frontend-ypmt.onrender.com/)**

### ⚙️ Backend / API

**[Acessar API](https://consultorio-backend-00q5.onrender.com/)**

> A aplicação está hospedada em ambiente de produção utilizando Render, com banco de dados MySQL hospedado na nuvem.

---

## 🎯 Objetivo

Este projeto foi desenvolvido com o objetivo de aplicar, na prática, conceitos de desenvolvimento **backend e full stack**, trabalhando com:

* Desenvolvimento de APIs REST
* Persistência de dados
* Arquitetura em camadas
* Integração entre frontend e backend
* Validação de dados
* Tratamento global de exceções
* Testes automatizados
* Docker
* Deploy e configuração de ambiente
* Controle de versão com Git e GitHub

---

## ✨ Funcionalidades

### 👤 Pacientes

* Cadastro de pacientes
* Consulta de pacientes
* Atualização de dados
* Exclusão de pacientes
* Busca por identificação

### 👨‍⚕️ Médicos

* Cadastro de médicos
* Consulta de médicos
* Busca por nome
* Busca por especialidade
* Atualização de dados
* Exclusão de médicos

### 🏥 Especialidades

* Cadastro de especialidades
* Consulta de especialidades
* Gerenciamento das especialidades médicas

### 📅 Agendamentos

* Criação de consultas
* Gerenciamento de horários
* Associação entre paciente, médico e especialidade
* Controle da fila de espera
* Processamento da fila de atendimento
* Remarcação e gerenciamento de consultas

### 🗓️ Agenda

* Cadastro de horários dos médicos
* Consulta de agendas
* Organização dos horários por dia da semana
* Gerenciamento da disponibilidade dos médicos

---

## 🛠️ Tecnologias utilizadas

### Frontend

* React
* JavaScript
* Vite
* React Router
* Axios
* Tailwind CSS

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven
* MySQL
* JUnit
* Mockito

### Infraestrutura e ferramentas

* Docker
* Docker Compose
* Git
* GitHub
* GitHub Actions
* Render
* Aiven

---

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada na separação de responsabilidades:

```text
Frontend (React)
       │
       │ HTTP / REST
       ▼
Backend (Spring Boot)
       │
       │ JPA / Hibernate
       ▼
MySQL
```

O frontend é responsável pela interface e interação com o usuário, enquanto o backend concentra as regras de negócio e disponibiliza os endpoints REST.

---

## 📁 Estrutura do projeto

```text
consultorio/
│
├── Back/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── docker-compose.yaml
│
├── Front/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

---

## 🧪 Testes

O backend possui testes automatizados utilizando **JUnit e Mockito**, incluindo testes de:

* Controllers
* Services
* Regras de negócio
* Integração da aplicação

Atualmente o projeto possui **73 testes automatizados**, todos passando na versão preparada para deploy.

```text
Tests run: 73
Failures: 0
Errors: 0
Skipped: 0
```

---

## 🐳 Docker

O backend possui configuração para execução utilizando Docker.

Exemplo de execução:

```bash
docker compose up -d
```

Também existe um `Dockerfile` utilizado para empacotar a aplicação Spring Boot para o ambiente de produção.

---

## 💻 Como executar localmente

### Pré-requisitos

* Java 17+
* Maven
* Node.js
* npm
* Docker (opcional)
* MySQL

### Backend

Entre na pasta:

```bash
cd Back
```

Execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### Frontend

Entre na pasta:

```bash
cd Front
```

Instale as dependências:

```bash
npm install
```

Execute:

```bash
npm run dev
```

O frontend será disponibilizado pelo Vite em ambiente de desenvolvimento.

---

## 🌐 Comunicação entre Frontend e Backend

O frontend consome a API do backend utilizando requisições HTTP através do **Axios**.

Em produção, o frontend está configurado para utilizar a API hospedada no Render:

```text
https://consultorio-backend-00q5.onrender.com
```

---

## 📌 Deploy

A versão `consultorio-deploy` foi preparada especificamente para disponibilização da aplicação em ambiente online.

### Frontend

Hospedado no **Render**:

```text
https://consultorio-frontend-ypmt.onrender.com/
```

### Backend

Hospedado no **Render** utilizando Docker:

```text
https://consultorio-backend-00q5.onrender.com/
```

### Banco de dados

MySQL hospedado na **Aiven**.

---

## 🔀 Branches

O projeto utiliza branches para separar diferentes etapas do desenvolvimento.

* `main` — versão principal do projeto
* `consultorio-deploy` — versão preparada para deploy e demonstração
* `feature/security` — desenvolvimento das funcionalidades de autenticação e autorização
* `feature/tests` — desenvolvimento relacionado aos testes
* `feature/github-actions` — configuração de integração contínua

---

## 👨‍💻 Autor

**Iago Dias Prudencio Pereira**

Estudante de Engenharia de Software com foco em desenvolvimento **Backend Java e Spring Boot**.

* GitHub: [IagoDiasPP](https://github.com/IagoDiasPP)
* Projeto: [Consultorio](https://github.com/IagoDiasPP/consultorio)

