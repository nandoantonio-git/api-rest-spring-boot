# AutoEscola3ESR — Checkpoint 5 SOA e WebServices

API REST para gerenciamento de uma autoescola, com cadastro de instrutores, alunos, usuários e instruções, agendamento/cancelamento, autenticação JWT, documentação OpenAPI, CORS e integração externa ViaCEP.

## Integrantes

- Fernando Luiz Silva Antonio — RM555201

## Tecnologias

- Java 17 ou superior compatível (**o `pom.xml` do projeto está configurado para Java 25**; consulte [Versão do Java](#versão-do-java))
- Spring Boot 4.0.5
- Spring MVC, Spring Data JPA e Spring Security
- MySQL e Flyway
- JWT com java-jwt
- Springdoc OpenAPI / Swagger UI
- JUnit, Spring Test e MockRestServiceServer

## Arquitetura

O projeto mantém uma arquitetura modular com separação entre:

- `adapter/in`: controllers e contratos HTTP;
- `application`: serviços, domínio e portas;
- `adapter/out`: persistência e integrações externas;
- `shared`: value objects e componentes compartilhados.

A integração ViaCEP está atrás da porta `EnderecoService`, implementada por `ViaCepClient`, evitando acoplamento do domínio ao fornecedor externo.

## Configuração

Configure as variáveis de ambiente antes de iniciar:

**macOS / Linux (bash):**

```bash
export DB_URL='jdbc:mysql://localhost:3306/autoescola3esr'
export DB_USER='root'
export DB_PASSWORD='sua-senha-local'
export JWT_SECRET='um-segredo-local-com-pelo-menos-32-caracteres'
export CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5500'
```

**Windows (Prompt de Comando / CMD):**

```cmd
set "DB_URL=jdbc:mysql://localhost:3306/autoescola3esr?createDatabaseIfNotExist=true"
set "DB_USER=root"
set "DB_PASSWORD=sua-senha-local"
set "JWT_SECRET=um-segredo-local-com-pelo-menos-32-caracteres"
set "CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5500"
```

**Windows (PowerShell):**

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/autoescola3esr?createDatabaseIfNotExist=true"
$env:DB_USER="root"
$env:DB_PASSWORD="sua-senha-local"
$env:JWT_SECRET="um-segredo-local-com-pelo-menos-32-caracteres"
$env:CORS_ALLOWED_ORIGINS="http://localhost:3000,http://localhost:5500"
```

O parâmetro `createDatabaseIfNotExist=true` permite que o MySQL crie o banco caso ele ainda não exista, **desde que o usuário possua permissão**. Sem esse parâmetro, crie previamente o banco `autoescola3esr`.

As variáveis definidas com `export`, `set` ou `$env:` valem para a sessão de terminal correspondente. Execute o Maven no mesmo terminal.

**Segurança:** nunca faça commit de senha, token, `.env` ou outro segredo. Substitua a senha inicial de teste do administrador e utilize um `JWT_SECRET` forte em ambientes compartilhados ou de produção.

O MySQL deve estar disponível e o usuário precisa ter permissão para criar/alterar as tabelas usadas pelo Flyway.

## Versão do Java

A compilação depende de um **JDK**, que inclui o compilador `javac`. Ter apenas um JRE instalado não é suficiente. Confira o ambiente com:

```bash
java -version
javac -version
```

O projeto está configurado para Java 25 no `pom.xml`. **A execução local com JDK 17 foi validada usando uma propriedade Maven temporária**, sem alterar o arquivo:

**macOS / Linux:**

```bash
bash mvnw spring-boot:run -Djava.version=17
```

**Windows (CMD ou PowerShell):**

```cmd
mvnw.cmd spring-boot:run -Djava.version=17
```

Esse parâmetro configura a propriedade `java.version` **somente para essa execução**; não instala ou seleciona um JDK automaticamente. O próprio Maven precisa estar rodando em um JDK compatível. Verifique com `mvnw.cmd -version` no Windows ou `bash mvnw -version` no macOS/Linux.

Se o Windows continuar usando um Java antigo apesar de haver JDK 17 instalado, é possível ajustar **somente a sessão atual do CMD**, substituindo o caminho pelo diretório real do JDK:

```cmd
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
java -version
javac -version
```

Não é necessário remover o Java antigo ou editar o `pom.xml` para aplicar esse ajuste temporário. Para builds que seguem estritamente a configuração original do projeto, utilize o JDK correspondente ao `pom.xml`.

## Executar

**macOS / Linux:**

```bash
bash mvnw spring-boot:run
```

**Windows:**

```cmd
mvnw.cmd spring-boot:run
```

Se estiver usando JDK 17 com a configuração atual do `pom.xml`, acrescente `-Djava.version=17`, conforme a seção anterior.

Por padrão, a aplicação inicia em `http://localhost:8081`. Mantenha o terminal aberto enquanto utilizar a API; para encerrar, pressione `Ctrl+C`.

Na primeira inicialização de um banco vazio, confira no log do Flyway a aplicação das **11 migrations**, incluindo V10 e V11. Em um banco já migrado, o Flyway deverá validar o histórico, não aplicar novamente as mesmas migrations.

## Testar

Build sem executar testes (usa a configuração Java do projeto):

```bash
bash mvnw -q -DskipTests package
```

Suíte completa:

```bash
bash mvnw clean verify
```

Teste isolado da integração ViaCEP, sem depender da internet:

```bash
bash mvnw -Dtest=ViaCepClientTest test
```

No Windows, substitua `bash mvnw` por `mvnw.cmd`. Se utilizar JDK 17 e a configuração atual do `pom.xml`, adicione `-Djava.version=17` aos comandos Maven conforme necessário.

Os testes de persistência/controller precisam do banco de teste configurado em `application-test.properties`.

### Validação manual local (Windows / JDK 17)

O fluxo principal foi verificado manualmente em um ambiente com Java 17, Spring Boot 4.0.5 e MySQL 8.0.43:

- inicialização da API na porta 8081 e aplicação de 11 migrations (V1–V11);
- login do administrador com emissão de JWT;
- cadastro de instrutor e aluno, ambos com resposta `201 Created`;
- consulta `GET /instrucoes` com resposta `200 OK`;
- agendamento de instrução e cancelamento com resposta `204 No Content`;
- confirmação no MySQL de que a instrução cancelada manteve o registro e recebeu o motivo `ALUNO_DESISTIU`.

Essas verificações manuais **não substituem a suíte automatizada** nem comprovam todos os cenários de validação, concorrência e integração externa.

## Swagger/OpenAPI

Com a aplicação em execução:

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- OpenAPI YAML: `http://localhost:8081/v3/api-docs.yaml`

Faça `POST /login`, copie **apenas o valor** retornado no campo `tokenJWT` e clique em **Authorize**. Na configuração HTTP Bearer do Swagger UI, normalmente basta colar o token, **sem** `{ "tokenJWT": ... }` e **sem** o prefixo `Bearer`; a própria interface monta o cabeçalho `Authorization: Bearer <JWT>`.

Em chamadas manuais com `curl`, informe o cabeçalho completo `Authorization: Bearer SEU_TOKEN`.

### Formato de data e hora no agendamento

O campo `data_hora` é convertido para `LocalDateTime` com um padrão personalizado. **No ambiente testado**, o formato aceito foi `dd/MM/yyyy - HH:mm`, por exemplo:

```json
{
  "id_aluno": 1,
  "id_instrutor": 1,
  "especialidade": "MOTOS",
  "data_hora": "23/10/2026 - 14:00"
}
```

O exemplo automático do Swagger com `2026-10-23T14:00:00.000Z` gerou erro de desserialização nesse ambiente. Não envie o sufixo `Z` ou um timestamp UTC para esse campo sem verificar a configuração de conversão. O valor `LocalDateTime` não contém informação de fuso horário.

## Principais endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/login` | Autenticação e emissão do JWT |
| GET/POST | `/instrutores` | Listagem e cadastro de instrutores |
| GET/PUT/DELETE | `/instrutores/{id}` ou `/instrutores` | Detalhamento, atualização e inativação |
| GET/POST | `/alunos` | Listagem e cadastro de alunos |
| GET/PUT/DELETE | `/alunos/{id}` ou `/alunos` | Detalhamento, atualização e inativação |
| GET/POST | `/usuarios` | Listagem e cadastro de usuários (perfil ADMIN) |
| GET/PUT/DELETE | `/usuarios/{id}` ou `/usuarios` | Detalhamento, atualização e inativação (perfil ADMIN) |
| GET/POST | `/instrucoes` | Listagem das instruções ativas e agendamento |
| GET | `/instrucoes/{id}` | Detalhamento de instrução |
| DELETE | `/instrucoes` | Cancelamento de instrução, com motivo no corpo |
| GET | `/enderecos/{cep}` | Consulta de endereço no ViaCEP |
| GET | `/health-check` | Verificação de disponibilidade |

## Integração externa ViaCEP

Exemplo:

```bash
curl -H 'Authorization: Bearer SEU_TOKEN' \
  http://localhost:8081/enderecos/01311-000
```

O client normaliza o CEP, aplica timeout de conexão/leitura, trata CEP inválido, CEP inexistente e falhas HTTP. Os testes usam resposta simulada e não dependem da API pública.

## Regras de agendamento

O agendamento valida:

- horário de funcionamento: segunda a sábado, das 06:00 às 21:00 (instruções de 1 hora, com início até 20:00);
- horário em hora inteira (ex.: 09:00, 13:00);
- antecedência mínima de 30 minutos;
- aluno e instrutor existentes e ativos;
- no máximo duas instruções por dia para o mesmo aluno;
- instrutor sem outra instrução na mesma data/hora;
- instrutor opcional: se não for informado, o sistema escolhe aleatoriamente um instrutor disponível da especialidade informada.

## Regras de cadastro

- Instrutor: nome, e-mail, telefone, CNH, especialidade (`MOTOS`, `CARROS`, `VANS`, `CAMINHOES`) e endereço completo são obrigatórios; apenas número e complemento do endereço são opcionais.
- Aluno: nome, e-mail, telefone, CPF e endereço completo são obrigatórios; apenas número e complemento do endereço são opcionais.
- Listagens ordenadas por nome, paginadas com 10 registros. Instrutores exibem nome, e-mail, CNH e especialidade; alunos exibem nome, e-mail e CPF.
- Atualização: somente nome, telefone e endereço. Enviar e-mail, CNH ou especialidade (instrutor), ou e-mail ou CPF (aluno), retorna `422`.
- Exclusão: não apaga os dados, apenas inativa o registro.

A migration `V9__add_indexes_for_instruction_scheduling.sql` adiciona índices para consulta. A partir da `V10`, a restrição de dupla reserva do instrutor é garantida pelos validadores, que ignoram instruções canceladas, permitindo reagendar um horário liberado.

### Cancelamento

O cancelamento exige motivo (`ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU` ou `OUTROS`) e antecedência mínima de 24 horas. A instrução não é apagada: fica registrada com o motivo e deixa de aparecer na listagem e de ocupar o horário.

```bash
curl -X DELETE -H 'Authorization: Bearer SEU_TOKEN' -H 'Content-Type: application/json' \
  -d '{ "id_instrucao": 1, "motivo": "ALUNO_DESISTIU" }' \
  http://localhost:8081/instrucoes
```

## Usuários

O CRUD de `/usuarios` é restrito ao perfil `ADMIN`. A senha é gravada com BCrypt e nunca é retornada. A exclusão inativa o usuário, que passa a ter o login recusado.

O primeiro administrador precisa ser provisionado de forma controlada no banco quando não existe usuário inicial. Se for utilizado um administrador de testes, troque a senha antes de utilizar o ambiente para dados reais e não publique hashes ou credenciais iniciais como configuração de produção.

## CORS

As origens permitidas são definidas por `CORS_ALLOWED_ORIGINS`. O padrão local permite `http://localhost:3000` e `http://localhost:5500`. Em produção, informe somente as origens reais do frontend.
