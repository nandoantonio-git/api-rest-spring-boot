alter table instrucoes add column motivo_cancelamento varchar(100);

create index idx_instrucoes_instrutor_data_hora
    on instrucoes(instrutor_id, data_hora);

alter table instrucoes drop index uk_instrucoes_instrutor_data_hora;
