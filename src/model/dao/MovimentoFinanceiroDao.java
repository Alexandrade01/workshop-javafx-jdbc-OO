package model.dao;

import java.util.List;

import model.entities.MovimentoFinanceiro;

public interface MovimentoFinanceiroDao {
	
	void insert(MovimentoFinanceiro obj);
	List<MovimentoFinanceiro> findByUserId(Integer id);
	void deleteById(Integer id);
	void update(MovimentoFinanceiro entity);
	void updateSaldo(Double saldo, Integer id);

}
