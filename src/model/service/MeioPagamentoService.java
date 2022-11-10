package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MeioPagamentoDao;
import model.entities.Categoria;
import model.entities.MeioPagamento;

public class MeioPagamentoService {
	
private MeioPagamentoDao dao = DaoFactory.createMeioPagamentoDao();
	
	public List<MeioPagamento> findAll() {
		
		return dao.findAll();
	}

	public void saveOrUpdate(MeioPagamento obj) {

		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Categoria obj) {
		
		dao.deleteById(obj.getId());
		
	}

}
