alter table usuarios add column ativo tinyint default 1;
update usuarios set ativo = 1;
