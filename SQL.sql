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
