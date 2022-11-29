create database fesawallet;
use fesawallet;

#TABELAS
create table usuario (
id int(11) NOT NULL AUTO_INCREMENT,
nome varchar(100) NOT NULL,
sobrenome varchar(100) NOT NULL,
email varchar(100) NOT NULL,
senha varchar(100) NOT NULL,
PRIMARY KEY (id)
);

create table meiopagamento(
id int(11) NOT NULL AUTO_INCREMENT,
descricao varchar(100) NOT NULL,
saldo double,
usuarioId int(11),
PRIMARY KEY (id),
FOREIGN KEY (usuarioId) REFERENCES usuario (id)
);

create table categoria (
id INT(11) NOT NULL AUTO_INCREMENT,
descricao VARCHAR(100) NOT NULL,
tipoDeMovimento ENUM('RECEITA','DESPESA'),
idUsuario int(11) NOT NULL,

PRIMARY KEY (id),
FOREIGN KEY (idUsuario) REFERENCES usuario (id)
);

create table movimentofinanceiro(
id int(11) NOT NULL AUTO_INCREMENT,
descricao varchar(100) NOT NULL,
dataTransacao DATE NOT NULL,
valor double NOT NULL,
categoriaId int(11) NOT NULL,
meiopagamentoId int(11) NOT NULL,
usuarioId int(11) NOT NULL,

PRIMARY KEY (id),
FOREIGN KEY (categoriaId) REFERENCES categoria (id),
FOREIGN KEY (meiopagamentoId) REFERENCES meiopagamento (id),
FOREIGN  KEY (usuarioId) REFERENCES usuario (id)
);

#PROCEDURES
DELIMITER $$
CREATE PROCEDURE aumentaCash(aumenta double, idmeiopagamento int)
BEGIN
   UPDATE meiopagamento SET saldo = (saldo + aumenta) where id = idmeiopagamento ;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE diminuiCash(diminui double, idmeiopagamento int)
BEGIN
   UPDATE meiopagamento SET saldo = (saldo - diminui) where id = idmeiopagamento ;
END $$
DELIMITER ;

#DMLS

insert into movimentofinanceiro (descricao, dataTransacao, valor, categoriaId, meiopagamentoId, usuarioId  ) 
VALUES ('teste','1998-04-21 00:00:00',2020,1,1,1);

INSERT INTO usuario (nome, sobrenome, email, senha)
VALUES ('Ale','Andrade','ale90@hotmail.com','senha'),
('ze1','alves1','ale90@hotmail.com','senha');

insert into categoria (descricao, tipoDeMovimento, idUsuario)
VALUES ('ROUPAS','DESPESA',1), ('SALARIO','RECEITA',1);

#COMANDOS
SELECT usuario.* from usuario WHERE usuario.nome = 'Ale' and usuario.senha = 'senha';

select descricao from categoria where id = 1

SELECT movimentofinanceiro.*, categoria.*, meiopagamento.*
FROM movimentofinanceiro 
INNER JOIN categoria ON movimentofinanceiro.categoriaId = categoria.id
INNER JOIN meiopagamento ON movimentofinanceiro.meiopagamentoId = meiopagamento.id
WHERE movimentofinanceiro.id = 1;

select * from categoria where tipoDeMovimento = 'RECEITA' order by id;

SELECT movimentofinanceiro.* , categoria.descricao as CATNAME , meiopagamento.descricao as MPNAME 
FROM movimentofinanceiro 
INNER JOIN categoria ON movimentofinanceiro.categoriaId = categoria.id 
INNER JOIN meiopagamento ON movimentofinanceiro.meiopagamentoId = meiopagamento.id 
WHERE movimentofinanceiro.usuarioId = 1

select * from usuario

SELECT * FROM categoria WHERE tipoDeMovimento = 'DESPESA' AND idUsuario = 7 ORDER BY id

SELECT * FROM meiopagamento WHERE usuarioId = 7 ORDER BY id

CALL diminuiCash(500,2);

select * from movimentofinanceiro

SELECT SUM(movimentofinanceiro.valor) from movimentofinanceiro 
INNER JOIN categoria ON categoria.id = movimentofinanceiro.categoriaId
WHERE movimentofinanceiro.usuarioId = 1 AND categoria.tipoDeMovimento = 'DESPESA'

select * from meiopagamento

SELECT SUM(saldo) FROM meiopagamento WHERE usuarioId = 1