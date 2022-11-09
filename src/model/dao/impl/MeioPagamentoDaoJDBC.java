package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.MeioPagamentoDao;
import model.entities.Department;
import model.entities.MeioPagamento;

public class MeioPagamentoDaoJDBC implements MeioPagamentoDao {

	private Connection conn;
	
	public MeioPagamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public MeioPagamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM meiopagamento WHERE id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				MeioPagamento obj = new MeioPagamento();
				obj.setId(rs.getInt("id"));
				obj.setDescricao(rs.getString("descricao"));
				obj.setSaldo(rs.getDouble("saldo"));
				obj.setUsuarioId(rs.getInt("usuarioId"));
				return obj;
			}
			return null;
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
	public List<MeioPagamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM meiopagamento ORDER BY id");
			rs = st.executeQuery();

			List<MeioPagamento> list = new ArrayList<>();

			while (rs.next()) {
				MeioPagamento obj = new MeioPagamento();
				obj.setId(rs.getInt("id"));
				obj.setDescricao(rs.getString("descricao"));
				obj.setSaldo(rs.getDouble("saldo"));
				obj.setUsuarioId(rs.getInt("usuarioId"));
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
	public void insert(MeioPagamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO meiopagamento " +
				"(descricao, saldo, usuarioId) " +
				"VALUES " +
				"(?, ?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricao());
			st.setDouble(2, obj.getSaldo());
			st.setInt(3, obj.getUsuarioId());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(MeioPagamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE meiopagamento " +
				"SET descricao = ?, saldo = ?, usuarioId = ?  " +
				"WHERE id = ?");

			st.setString(1, obj.getDescricao());
			st.setDouble(2, obj.getSaldo());
			st.setInt(3, obj.getUsuarioId());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

}
