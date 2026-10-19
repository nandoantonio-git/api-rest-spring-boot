# AutoEscola3ESR — Checkpoint 5 SOA e WebServices

API REST para gerenciamento de uma autoescola, com cadastro de instrutores, alunos, usuários e instruções, agendamento/cancelamento, autenticação JWT, documentação OpenAPI, CORS e integração externa ViaCEP.

## Integrantes

- Fernando Luiz Silva Antonio — RM555201

## Tecnologias

- Java 17 ou superior (o ambiente atual usa Java 25)
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

```bash
export DB_URL='jdbc:mysql://localhost:3306/autoescola3esr'
export DB_USER='root'
export DB_PASSWORD='sua-senha-local'
export JWT_SECRET='um-segredo-local-com-pelo-menos-32-caracteres'
export CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5500'
```

Nunca faça commit de senha, token, `.env` ou outro segredo.

O MySQL deve estar disponível e o usuário precisa ter permissão para criar/alterar as tabelas usadas pelo Flyway.

## Executar

```bash
bash mvnw spring-boot:run
```

Por padrão, a aplicação inicia em `http://localhost:8081`.

## Testar

Build sem executar testes de integração:

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

Os testes de persistência/controller precisam do banco de teste configurado em `application-test.properties`.

## Swagger/OpenAPI

Com a aplicação em execução:

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- OpenAPI YAML: `http://localhost:8081/v3/api-docs.yaml`

Use o botão **Authorize** e informe `Bearer <JWT>` para testar endpoints protegidos.

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

## CORS

As origens permitidas são definidas por `CORS_ALLOWED_ORIGINS`. O padrão local permite `http://localhost:3000` e `http://localhost:5500`. Em produção, informe somente as origens reais do frontend.
