package model.service;

import java.util.List;

import model.dao.CategoriaDao;
import model.dao.DaoFactory;
import model.entities.Categoria;

public class CategoriaService {

	// configuracao de banco de dados
	private CategoriaDao dao = DaoFactory.createCategoriaDao();

	public List<Categoria> findAllDepositByUserId(Integer usuarioId) {

		return dao.findAllDepositByUserId(usuarioId);
	}

	public List<Categoria> findAllOutsByUserId(Integer usuarioId) {

		return dao.findAllOutsByUserId(usuarioId);
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

	public List<Categoria> findByUserId(Integer userId) {

		return dao.findByUserId(userId);

	}

	public String findNameByUserId(Integer userId) {

		return dao.findNameByUserId(userId);

	}

}
