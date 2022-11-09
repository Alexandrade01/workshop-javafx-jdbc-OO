package model.service;

import java.util.List;

import model.dao.CategoriaDao;
import model.dao.DaoFactory;
import model.entities.Categoria;
import model.entities.Seller;

public class CategoriaService {
	
	private CategoriaDao dao = DaoFactory.createCategoriaDao();
	
	public List<Categoria> findAll() {
		
		return dao.findAll();
	}

	public void saveOrUpdate(Categoria obj) {

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
