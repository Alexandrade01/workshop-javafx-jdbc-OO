package model.dao;

import java.util.List;

import model.entities.Categoria;

public interface CategoriaDao {
	
	void insert(Categoria dao);
	void update (Categoria dao);
	void deleteById(Integer id);
	List<Categoria> findByUserId(Integer id);
	String findNameByUserId(Integer id);
	List<Categoria> findAllOutsByUserId(Integer id);
	List<Categoria> findAllDepositByUserId(Integer usuarioId);

	


}
