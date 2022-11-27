package model.dao;

import java.util.List;

import model.entities.MeioPagamento;

public interface MeioPagamentoDao {
	
	void insert(MeioPagamento dao);
	void update (MeioPagamento dao);
	void deleteById(Integer id);
	MeioPagamento findById(Integer id);
	List<MeioPagamento> findAllByUserId(Integer usuarioId);
	List<MeioPagamento> findByUserId(Integer userId);
	Double totalReceitasById(Integer usuarioID);
	Double totalDespesasById(Integer usuarioID);


}
