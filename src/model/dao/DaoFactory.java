package model.dao;

import db.DB;
import model.dao.impl.CategoriaDaoJDBC;
import model.dao.impl.MeioPagamentoDaoJDBC;

public class DaoFactory {

	public static CategoriaDao createCategoriaDao() {

		return new CategoriaDaoJDBC(DB.getConnection());
	}

	public static MeioPagamentoDao createMeioPagamentoDao() {

		return new MeioPagamentoDaoJDBC(DB.getConnection());
	}
}