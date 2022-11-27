package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.CategoriaDao;
import model.entities.Categoria;
import model.enumerations.TipoDeMovimento;

public class CategoriaDaoJDBC implements CategoriaDao {

	private Connection conn;

	public CategoriaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Categoria obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO categoria " + "(descricao, tipoDeMovimento, idUsuario) " + "VALUES " + "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricao());
			st.setString(2, obj.getTipoDeMovimento().toString());
			st.setInt(3, obj.getIdUsuario());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Categoria obj) {

		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE categoria " + "SET descricao = ?, tipoDeMovimento = ? " + "WHERE id = ?");

			st.setString(1, obj.getDescricao());
			st.setString(2, obj.getTipoDeMovimento().toString());
			st.setInt(3, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM categoria WHERE id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public List<Categoria> findByUserId(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM categoria "
					+ "WHERE idUsuario = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			List<Categoria> list = new ArrayList<>();
			
			while(rs.next()) {
				
				Categoria obj = new Categoria();
				obj = instantiateCategoria(rs);
				list.add(obj);
			}
			
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Categoria> findAllDeposit() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM categoria WHERE tipoDeMovimento = 'RECEITA' ORDER BY id");

			rs = st.executeQuery();

			List<Categoria> list = new ArrayList<>();

			while (rs.next()) {

				Categoria obj = new Categoria();
				obj = instantiateCategoria(rs);
				list.add(obj);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Categoria> findAllOut() {
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM categoria WHERE tipoDeMovimento = 'DESPESA' ORDER BY id");

			rs = st.executeQuery();

			List<Categoria> list = new ArrayList<>();

			while (rs.next()) {

				Categoria obj = new Categoria();
				obj = instantiateCategoria(rs);
				list.add(obj);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public String findNameByUserId(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT descricao FROM categoria WHERE id = ?");
			
			st.setInt(1, 1);
			
			rs = st.executeQuery();

			while (rs.next()) {

//				Categoria obj = new Categoria();
//				obj = instantiateCategoria(rs);
//				return obj.getDescricao();
				
				String retorno = rs.getString("descricao");
				return retorno;
			}

			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Categoria instantiateCategoria(ResultSet rs) throws SQLException {
		Categoria obj = new Categoria();
		obj.setId(rs.getInt("id"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setTipoDeMovimento(TipoDeMovimento.valueOf(rs.getString("tipoDeMovimento")));
		obj.setIdUsuario(rs.getInt("idUsuario"));
		return obj;
	}
}
