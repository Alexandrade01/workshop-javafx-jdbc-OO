package model.dao;

import java.util.List;

import model.entities.MeioPagamento;

public interface MeioPagamentoDao {
	
	void insert(MeioPagamento dao);
	void update (MeioPagamento dao);
	void deleteById(Integer id);
	MeioPagamento findById(Integer id);
	List<MeioPagamento> findAll();
	List<MeioPagamento> findByUserId(Integer userId);

}
