# AutoEscola3ESR — Checkpoint 5 SOA e WebServices

API REST para gerenciamento de uma autoescola, com cadastro de instrutores, alunos, usuários e instruções, agendamento/cancelamento, autenticação JWT, documentação OpenAPI, CORS e integração externa ViaCEP.

## Integrantes

- Fernando Luiz Silva Antonio — RM555201
- [Preencher nome e RM do integrante 2]
- [Preencher nome e RM do integrante 3]
- [Preencher nome e RM do integrante 4]
- [Preencher nome e RM do integrante 5]

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
| POST | `/instrucoes` | Agendamento de instrução |
| DELETE/PATCH | `/instrucoes/{id}` | Cancelamento, conforme contrato implementado |
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

O fluxo valida, entre outras regras do projeto:

- instrutor ativo;
- conflito de horário do instrutor;
- horário de funcionamento;
- antecedência mínima;
- limite diário do aluno;
- horário em hora inteira;
- aluno e instrutor existentes.

A migration `V9__add_indexes_for_instruction_scheduling.sql` adiciona índices para consulta e uma restrição única para evitar dupla reserva do mesmo instrutor no mesmo horário.

## CORS

As origens permitidas são definidas por `CORS_ALLOWED_ORIGINS`. O padrão local permite `http://localhost:3000` e `http://localhost:5500`. Em produção, informe somente as origens reais do frontend.

## Observações para a entrega

- Preencher os nomes/RMs restantes dos integrantes antes da publicação.
- Revisar o contrato de cancelamento com o documento oficial do checkpoint.
- Confirmar com o professor a data de entrega informada no enunciado.
- Publicar apenas o código, README e evidências solicitadas; não publicar segredos ou dumps do banco.
