# 🏢 Condomínio Digital

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

Um sistema web completo para a gestão inteligente e digital de condomínios. Desenvolvido como projeto acadêmico, o sistema permite o controle de encomendas, comunicados no mural, cadastro de moradores e controle de acessos (Porteiros e Moradores).

---

## 🌟 Funcionalidades

* **📦 Gestão de Encomendas:** Controle de entrada e saída de pacotes, com atualização de status (Pendente, Entregue, Devolvida).
* **📢 Mural de Avisos:** Feed de comunicados importantes na tela inicial para manter todos os moradores informados.
* **🏢 Cadastro de Unidades e Moradores:** Gerenciamento completo dos apartamentos, blocos e dados de contato dos residentes.
* **🚚 Transportadoras:** Cadastro e controle das empresas que realizam as entregas no condomínio.
* **🔐 Autenticação e Segurança (Spring Security):** Níveis de acesso diferentes. Porteiros possuem acesso total ao sistema (CRUD), enquanto moradores possuem visão limitada às suas pendências.
* **📄 Relatórios PDF:** Geração de relatórios gerenciais em PDF de todas as encomendas por unidade utilizando JasperReports.
* **📱 Interface Responsiva:** Design moderno feito com Tailwind CSS e Alpine.js, totalmente adaptado para funcionar tanto no computador quanto em telas de celulares.

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Linguagem:** Java 17
- **Framework:** Spring Boot (Web, Data JPA, Security)
- **Banco de Dados:** PostgreSQL
- **Relatórios:** JasperReports
- **Build Tool:** Maven

### Frontend
- **Template Engine:** Thymeleaf
- **Estilização:** Tailwind CSS (via Tailwind Maven Plugin)
- **Interatividade:** HTMX e Alpine.js
- **Ícones e SVG:** Componentes nativos e Bootstrap Icons

### Infraestrutura
- **Containers:** Docker e Docker Compose

---

## 🚀 Como Executar o Projeto

A aplicação foi totalmente conteinerizada para facilitar a execução sem a necessidade de instalar Java ou PostgreSQL localmente. Tudo o que você precisa é o Docker instalado na sua máquina.

### Pré-requisitos
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/) instalados.

### Passos para rodar

1. Clone este repositório para a sua máquina:
   ```bash
   git clone https://github.com/SEU_USUARIO/condominio-digital.git
   cd condominio-digital
   ```

2. Na raiz do projeto, execute o comando de build e deploy do Docker:
   ```bash
   docker compose up --build -d
   ```

3. O Docker fará o download do banco de dados, compilará todo o código Java e os estilos do Tailwind, e subirá os servidores. Aguarde alguns segundos.

4. Acesse a aplicação no seu navegador:
   * **URL:** [https://localhost:8443](https://localhost:8443)
   * *(Nota: Por estar utilizando um certificado SSL autoassinado gerado para ambiente de desenvolvimento (HTTPS local), o navegador pode exibir um aviso de segurança. Basta clicar em "Avançado" e prosseguir).*

### 🔑 Credenciais Padrão
*(O sistema as cria automaticamente no primeiro acesso)*
- **Administrador / Porteiro:** Usuário: `admin` | Senha: `123456`
- **Morador:** Usuário: `user` | Senha: `123456`

---

## 📁 Estrutura do Projeto

A arquitetura do projeto segue o padrão **MVC (Model-View-Controller)**:

- `src/main/java/.../model/` - Entidades e mapeamento relacional (JPA/Hibernate)
- `src/main/java/.../repository/` - Interfaces de acesso ao banco de dados (Spring Data JPA)
- `src/main/java/.../service/` - Regras de negócio e lógicas complexas
- `src/main/java/.../controller/` - Controladores REST e mapeamento de rotas web
- `src/main/resources/templates/` - Telas do sistema em HTML (Thymeleaf)
- `src/main/resources/application.properties` - Configurações globais do sistema e portas

---

## ✒️ Autor

Desenvolvido por **Raissa** como trabalho acadêmico.
