package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MovimentoFinanceiroDao;
import model.entities.MeioPagamento;
import model.entities.MovimentoFinanceiro;

public class MovimentoFinanceiroService {
	
	private MovimentoFinanceiroDao dao = DaoFactory.createMovimentoFinanceiroDao();
	
	public List<MovimentoFinanceiro> findByUserId(Integer usuarioID) {
		
		return dao.findByUserId(usuarioID);
		
	}

	public void remove(MovimentoFinanceiro obj) {

		dao.deleteById(obj.getId());
		
	}

}
