package model.dao;

import db.DB;
import model.dao.impl.CategoriaDaoJDBC;

public class DaoFactory {
	
	public static CategoriaDao createCategoriaDao() {
		
		return new CategoriaDaoJDBC(DB.getConnection());
	}
}