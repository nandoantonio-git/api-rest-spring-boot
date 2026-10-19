create unique index uk_instrucoes_instrutor_data_hora
    on instrucoes(instrutor_id, data_hora);

create index idx_instrucoes_aluno_data_hora
    on instrucoes(aluno_id, data_hora);

create index idx_instrutores_ativo_especialidade
    on instrutores(ativo, especialidade);
