package model.dao;

import java.util.List;

import model.entities.Categoria;

public interface CategoriaDao {
	
	void insert(Categoria dao);
	void update (Categoria dao);
	void deleteById(Integer id);
	List<Categoria> findByUserId(Integer id);
	List<Categoria> findAll();
	String findNameByUserId(Integer id);
	


}
