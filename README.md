# 🔧 Lode Manutenções

Sistema de gerenciamento de ordens de serviço para poços artesianos, desenvolvido como teste técnico para a vaga de Desenvolvedor Júnior.

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|---|---|
| Backend | Java 17 + Spring Boot 4 |
| Frontend | Vue.js 3 + Vite |
| Banco de Dados | PostgreSQL |
| Testes | JUnit 5 + Mockito |

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Java 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/) (ou use o `mvnw` incluído no projeto)
- [Node.js 20+](https://nodejs.org/)
- [PostgreSQL](https://www.postgresql.org/download/)

---

## 🗄️ Configuração do Banco de Dados

1. Abra o **pgAdmin** ou o terminal do PostgreSQL e crie o banco de dados:

```sql
CREATE DATABASE lode_db;
```

2. O projeto usa as seguintes credenciais por padrão (definidas em `application.properties`):

| Parâmetro | Valor padrão |
|---|---|
| Host | `localhost:5432` |
| Banco | `lode_db` |
| Usuário | `postgres` |
| Senha | `SUA_SENHA_AQUI` -> ADICIONE A SENHA DO SEU PostgreSQL AQUI.|

> Se as suas credenciais forem diferentes, edite o arquivo:
> `backend/manutencoes/src/main/resources/application.properties`

As tabelas são criadas **automaticamente** pelo Hibernate na primeira execução (`ddl-auto=update`).

---

## 🚀 Como Rodar

### 1. Backend (Spring Boot)

```bash
# Entre na pasta do backend
cd backend/manutencoes

# Execute o projeto
./mvnw spring-boot:run
```

> No Windows, use `mvnw.cmd spring-boot:run`

A API estará disponível em: `http://localhost:8080`

---

### 2. Frontend (Vue.js)

Abra um **novo terminal** e execute:

```bash
# Entre na pasta do frontend
cd frontend

# Instale as dependências
npm install

# Inicie o servidor de desenvolvimento
npm run dev
```

A aplicação estará disponível em: `http://localhost:5173`

---

## 🧪 Rodando os Testes Unitários

```bash
cd backend/manutencoes

./mvnw test
```

Os testes cobrem:
- ✅ Impedir o cadastro de equipamento sem nome
- ✅ Alterar o status de um equipamento com sucesso

---

## 📡 Endpoints da API

Base URL: `http://localhost:8080/api/equipamentos`

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/equipamentos` | Lista todos os equipamentos (paginado) |
| `GET` | `/api/equipamentos/{id}` | Busca um equipamento pelo ID |
| `POST` | `/api/equipamentos` | Cadastra um novo equipamento |
| `PATCH` | `/api/equipamentos/{id}/status?novoStatus=Crítico` | Altera o status |
| `DELETE` | `/api/equipamentos/{id}` | Remove um equipamento |

### Exemplo de corpo para o POST:

```json
{
  "nome": "Bomba Submersa 01",
  "tipo": "Bomba",
  "dataInstalacao": "2023-06-15",
  "status": "Operacional"
}
```

---

## 💡 Decisões Técnicas

**DTO (Data Transfer Object)**
Criado `EquipamentoDto` com `record` do Java para não expor a entidade JPA diretamente nas respostas da API, evitando a serialização de campos desnecessários.

**Injeção de Dependência via Construtor**
Utilizado `@RequiredArgsConstructor` do Lombok com campos `final` ao invés de `@Autowired`, que é a prática recomendada no Spring Boot moderno por tornar as dependências imutáveis e facilitar testes.

**Tratamento de Erros Centralizado**
`GlobalExceptionHandler` com `@RestControllerAdvice` captura exceções em toda a aplicação, evitando `try/catch` espalhados pelos controllers e padronizando as respostas de erro.

**Paginação**
Implementado `Pageable` na listagem de equipamentos para não sobrecarregar a API em cenários com muitos registros.

**CORS Centralizado**
Configurado via `WebConfig` com `@Value` lendo a origem permitida do `application.properties`, permitindo alterar o ambiente sem modificar o código-fonte.

---

## 🗃️ Dados de Teste

Para visualizar a aplicação funcionando com dados reais, você pode popular o banco de dados rapidamente. Execute o script abaixo no **pgAdmin** ou no terminal (`psql -U postgres -d lode_db`):

```sql
INSERT INTO equipamentos (nome, tipo, data_instalacao, status) VALUES
  ('Bomba Submersa Central',    'Bomba',      '2021-03-10', 'Operacional'),
  ('Bomba Auxiliar Poço 2',     'Bomba',      '2019-07-22', 'Manutenção Necessária'),
  ('Painel Elétrico Principal', 'Painel',     '2020-11-05', 'Operacional'),
  ('Painel de Controle Poço 3', 'Painel',     '2018-04-18', 'Crítico'),
  ('Tubulação Rede Norte',      'Tubulação',  '2022-01-30', 'Operacional'),
  ('Tubulação Setor Sul',       'Tubulação',  '2017-09-14', 'Manutenção Necessária'),
  ('Bomba de Pressão Reserva',  'Bomba',      '2023-06-01', 'Crítico'),
  ('Painel de Monitoramento',   'Painel',     '2024-02-20', 'Operacional');
```

---


### ✅ Resultado esperado no frontend

Após inserir os dados, ao abrir `http://localhost:5173` você verá:

- **4 equipamentos Operacionais** — cards em verde
- **2 equipamentos com Manutenção Necessária** — cards em amarelo
- **2 equipamentos Críticos** — cards em vermelho
- Filtro por nome funcionando em tempo real (ex: digitar `"Bomba"` exibe apenas as bombas)
