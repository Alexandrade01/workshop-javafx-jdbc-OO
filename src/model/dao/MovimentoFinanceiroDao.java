package model.dao;

import java.util.List;

import model.entities.MovimentoFinanceiro;

public interface MovimentoFinanceiroDao {
	
	void insert(MovimentoFinanceiro obj);
	List<MovimentoFinanceiro> findByUserId(Integer id);
	void deleteById(Integer id);
	void update(MovimentoFinanceiro entity);
	void diminuiSaldo(Double saldo, Integer id);
	void aumentaSaldo(Double saldo, Integer id);

}
