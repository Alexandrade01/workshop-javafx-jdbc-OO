package model.dao;

import java.util.List;

import model.entities.Categoria;

public interface CategoriaDao {
	
	void insert(Categoria dao);
	void update (Categoria dao);
	void deleteById(Integer id);
	Categoria findById(Integer id);
	List<Categoria> findAll();
	


}
