# 🍃 Monitoramento Alimentar

O **Sistema de Monitoramento Alimentar** é uma aplicação web desenvolvida como Trabalho de Conclusão de Curso (TCC) com o objetivo de auxiliar usuários no acompanhamento de seus hábitos alimentares e medidas corporais, promovendo maior controle sobre a saúde e o bem-estar físico.

A plataforma permite o **registro de medidas corporais**, o **cálculo automático do IMC (Índice de Massa Corporal)** e o **monitoramento da evolução física ao longo do tempo**, além de oferecer um **painel interativo** para visualização de dados e relatórios.

---

## 🧩 Objetivo Geral
Desenvolver um sistema web que possibilite o monitoramento de informações relacionadas à alimentação e composição corporal, contribuindo para o acompanhamento da saúde e incentivo a hábitos alimentares saudáveis.

## 🎯 Objetivos Específicos
- Implementar uma API REST utilizando o framework **Spring Boot** para gerenciar dados e regras de negócio;  
- Criar uma interface interativa com **React**, priorizando a experiência do usuário;  
- Armazenar as informações em um banco de dados **MySQL**;  
- Permitir o cálculo automático do **IMC** e a classificação conforme os parâmetros da **Organização Mundial da Saúde (OMS)**;
- Geração de relatórios com dados cadastrais do usuário

---

## 🧱 Arquitetura do Sistema

O sistema foi desenvolvido seguindo a arquitetura **cliente-servidor**, dividida em dois módulos principais:

### 🖥️ Back-End
- **Framework:** Spring Boot  
- **Linguagem:** Java  
- **Banco de Dados:** MySQL  
- **ORM:** JPA (Java Persistence API)  
- **Gerenciador de dependências:** Maven  

### 🌐 Front-End
- **Framework:** React  
- **Linguagem:** JavaScript  
- **Bibliotecas:** Material UI / Ant Design / Axios

### 📊 Tecnologias Utilizadas

| Camada | Tecnologia | Descrição |
|--------|-------------|-----------|
| Back-End | **Java / Spring Boot** | Criação da API REST e regras de negócio |
| Banco de Dados | **MySQL** | Armazenamento de dados dos usuários e medidas |
| Front-End | **React** | Interface interativa e responsiva |
| ORM | **JPA / Hibernate** | Mapeamento objeto-relacional |
| Build | **Maven** | Gerenciamento de dependências |
| Estilo | **Material UI / Ant Design** | Design moderno e responsivo |
| Hospedagem | **Hostinger / AWS (planejado)** | Implantação da aplicação e banco de dados |

---

### ⚙️Principais responsabilidades
- Criação e gerenciamento das entidades `Usuário`, `Medidas`, `Refeição` e `Alimento`;
- Implementação dos endpoints REST para operações CRUD;
- Cálculo e classificação do IMC;
- Integração com o front-end via API HTTP.

### ⚙️ Funcionalidades Principais
✅ Cadastro e autenticação de usuários  
✅ Registro de medidas corporais (altura, peso, tórax, cintura, quadril, etc.)  
✅ Cálculo automático do IMC e exibição da classificação  
✅ Registro e acompanhamento de refeições diárias 

---

### 🧠 Metodologia de Desenvolvimento
O projeto foi desenvolvido utilizando a **metodologia incremental**, permitindo a construção gradual das funcionalidades e testes contínuos de integração entre as camadas.  

**Etapas principais:**
1. Levantamento de requisitos e modelagem do banco de dados;  
2. Desenvolvimento da API e entidades JPA;  
3. Criação da interface e componentes React;  
4. Integração entre front-end e back-end;  
5. Testes unitários e de integração;  
6. Implantação e documentação final.

---

### 🚀 Instalação e Execução

### 🔧 Pré-requisitos
- Java 24  
- Node.js 25 
- MySQL  

### 🖥️ Back-End
```bash
# Clonar o repositório
git clone https://github.com/usuario/monitoramento-alimentar.git

# Entrar na pasta do backend
cd backend

# Executar a aplicação
mvn spring-boot:run
```

### 🖥️ Front-End
```bash

# Entrar na pasta do front-end
cd frontend

# Instalar dependências
npm install

# Executar o projeto
npm start
```

---

### 🖼️ EndPoints Principais

Cadastro e login autenticado

| Método | EndPoint | Descrição |
|--------|-------------|-----------|
| POST | **/api/auth/register** | Registrar novo usuário |
| POST | **/api/auth/login** | Autenticar usuário |

Operações com o cadastro do usuário

| Método | EndPoint | Descrição |
|--------|-------------|-----------|
| GET| **/api/usuarios/id** | Pesquisar o usuário por ID |
| GET | **/api/info/usuarios/id** | Pesquisar as informações do usuário por ID |
| PUT | **/api/editar/usuario/id** | Editar o usuário por ID |
| POST | **/api/cadastros/info/usuarios/novo** | Design moderno e responsivo |

Operações com medidas

| Método | EndPoint | Descrição |
|--------|-------------|-----------|
| GET | **/api/medida/id** | Retorna medida por ID |
| GET | **/api/medidas/id** | Retorna lista de medidas por ID do usuário |
| GET | **/api/medidas/imc/id** | Retorna IMC do usuário |
| POST | **/api/cadastros/medida/novo** | Registrar novas medidas |
| PUT | **/api/editar/medida/id** | Editar medida por ID |
| DELETE | **/api/medida/id** | Deletar medida por ID |

Operações com refeição

| Método | EndPoint | Descrição |
|--------|-------------|-----------|
| GET | **/api/refeicao/id** | Retorna refeicao por ID |
| GET | **/api/refeicoes/id** | Retorna lista de refeicoes por ID do usuário |
| POST | **/api/cadastros/refeicao/novo** | Registrar novas refeições |
| PUT | **/api/editar/refeicao/id** | Editar refeicao por ID |
| DELETE | **/api/refeicao/id** | Deletar refeicao por ID |

Operações com alimento

| Método | EndPoint | Descrição |
|--------|-------------|-----------|
| GET | **/api/alimento/id** | Retorna alimento por ID |
| PUT | **/api/editar/alimento/id** | Editar alimento por ID |
| DELETE | **/api/alimento/id** | Deletar alimento por ID |

Geração de relatórios

| Método | EndPoint | Descrição |
|--------|-------------|-----------|
| POST | **/api/medidas/download** | Gera relatório de medidas conforme json enviado |
| POST | **/api/refeicoes/download** | Gera relatório de refeições conforme json enviado | |

### 📈 Resultados Esperados

Com a implantação do sistema, espera-se que o usuário consiga acompanhar de forma prática e visual a evolução de suas medidas corporais e hábitos alimentares, utilizando os indicadores apresentados como apoio para uma rotina mais saudável e equilibrada.

---

### 📚 Referências Bibliográficas

WALLS, Craig. Spring Boot in Action. Manning Publications, 2016.</br>
React Documentation. Disponível em: https://react.dev</br>
Organização Mundial da Saúde (OMS). Índice de Massa Corporal (IMC). Disponível em: https://www.who.int</br>
ALURA Cursos Online.</br>
DEITEL, Paul; DEITEL, Harvey. Java: Como Programar. Pearson, 2017.</br>
