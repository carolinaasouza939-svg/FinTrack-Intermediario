# FinTrack — Controle de Finanças Pessoais (módulo intermediário)

Evolução do FinTrack de console para uma aplicação **JavaFX** com **PostgreSQL (JDBC)**,
**Generics** e testes **JUnit 5**.

## Funcionalidades

- Cadastrar receitas e despesas, **editar** e **remover** (com confirmação).
- Tabela de transações (data, descrição, tipo, valor) e saldo colorido.
- Tela de relatório (total de receitas, despesas, saldo e quantidade).
- Se o banco estiver fora do ar, o app avisa e roda em memória (nada é gravado).

## Estrutura

| Pacote | Conteúdo |
|---|---|
| `app` | `FinTrackApp` e `Launcher` (JavaFX), `Main` (console), `FabricaRepositorio` |
| `controller` | `FinTracker` (regras do sistema) |
| `model` | `Transacao`, `TransacaoMensal` (herança) |
| `exceptions` | `EntradaInvalidaException`, `RepositorioException` |
| `repository` | `Repositorio<T>` e `RepositorioGenerico<T>` (Generics e curingas) |
| `dao` | `TransacaoDAO` (JDBC), `FabricaConexao`, `ConexaoPostgres` |
| `utils` | `Formatador`, `UtilFinanceiro` |
| `view` | `Navegador`, controllers das 3 telas, `Alertas`, `Estilos` |

Telas em `src/main/resources/.../view` (`.fxml` + `fintrack.css`).

## Como rodar

1. No PostgreSQL, crie o banco `fintrack_db` e rode `sql/fintrack.sql`.
2. Copie `db.properties.example` para `db.properties` e coloque a sua senha
   (o arquivo está no `.gitignore`; a senha nunca vai para o GitHub).
   Alternativa: variáveis de ambiente `FINTRACK_DB_URL`, `FINTRACK_DB_USER`, `FINTRACK_DB_PASSWORD`.
3. Interface: `mvn javafx:run` (ou rode a classe `Launcher` na IDE).
   Console: classe `Main`.
4. Testes: `mvn test`.

Requisitos: JDK 21 e Maven (a IDE já traz). O JavaFX e os drivers vêm pelo `pom.xml`.

## Testes

- `TransacaoTest`: validações (valor, descrição, data), sinal do valor, `TransacaoMensal`.
- `RepositorioGenericoTest`: adicionar/remover/atualizar, cópia da lista, curingas `? extends T` e `? super T`.
- `FinTrackerTest`: saldo, totais por tipo, remover, editar, entradas inválidas.
- `TransacaoDAOTest`: DAO com **SQLite em memória** (inserir, listar, atualizar, remover, SQL Injection, banco fora do ar).
- `FormatadorTest`: moeda e data em formato brasileiro.
