-- migrations devem ser imutáveis, ou seja, uma migration que já foi executada, não pode ser executada novamente
alter table medicos
add telefone varchar(20) not null;