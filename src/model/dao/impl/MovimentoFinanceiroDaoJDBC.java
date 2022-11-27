package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.MovimentoFinanceiroDao;
import model.entities.Categoria;
import model.entities.MeioPagamento;
import model.entities.MovimentoFinanceiro;

public class MovimentoFinanceiroDaoJDBC implements MovimentoFinanceiroDao {

	private Connection conn;

	public MovimentoFinanceiroDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(MovimentoFinanceiro obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO movimentofinanceiro "
					+ "(descricao, dataTransacao, valor, categoriaId, meiopagamentoId, usuarioId) " + "VALUES "
					+ "(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescricao());
			st.setDate(2, new java.sql.Date(obj.getDataTransacao().getTime()));
			st.setDouble(3, obj.getValor());
			st.setInt(4, obj.getCategoria().getId());
			st.setInt(5, obj.getMeioPagamento().getId());
			st.setInt(6, obj.getUsuario());

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
	public List<MovimentoFinanceiro> findByUserId(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * , categoria.descricao as CATNAME , meiopagamento.descricao as MPNAME, meiopagamento.saldo as MPSALDO "
					+ "			FROM movimentofinanceiro "
					+ "			INNER JOIN categoria ON movimentofinanceiro.categoriaId = categoria.id "
					+ "			INNER JOIN meiopagamento ON movimentofinanceiro.meiopagamentoId = meiopagamento.id "
					+ "			WHERE movimentofinanceiro.usuarioId = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			List<MovimentoFinanceiro> list = new ArrayList<>();
			Map<Integer, Categoria> mapCategoria = new HashMap<>();
			Map<Integer, MeioPagamento> mapMeioPagamento = new HashMap<>();

			while (rs.next()) {
				
				Categoria cat =  mapCategoria.get(rs.getInt("categoriaId"));
				
				if(cat == null) {
					
					cat = instantiateCategoria(rs);
					mapCategoria.put(rs.getInt("categoriaId"), cat);
					
				}
				
				MeioPagamento mp = mapMeioPagamento.get(rs.getInt("meiopagamentoId"));
				
				if(mp == null) {
					
					mp = instantiateMeioPagamento(rs);
					mapMeioPagamento.put(rs.getInt("meiopagamentoId"), mp);
					
				}
				
				MovimentoFinanceiro mf = instantiateMovimentoFinanceiro(rs, cat, mp);

				list.add(mf);
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
	public void deleteById(Integer id) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM movimentofinanceiro WHERE id = ?");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
	
	private MovimentoFinanceiro instantiateMovimentoFinanceiro(ResultSet rs, Categoria cat, MeioPagamento mp) throws SQLException {
		
		MovimentoFinanceiro mf = new MovimentoFinanceiro();
		mf.setId(rs.getInt("id"));
		mf.setDescricao(rs.getString("descricao"));
		mf.setDataTransacao(new java.util.Date(rs.getTimestamp("dataTransacao").getTime()));
		mf.setValor(rs.getDouble("valor"));
		mf.setCategoria(cat);
		mf.setMeioPagamento(mp);
		mf.setUsuario(rs.getInt("usuarioId"));
		
		return mf;
		
	}
	
	private Categoria instantiateCategoria(ResultSet rs) throws SQLException {
		Categoria obj = new Categoria();
		obj.setId(rs.getInt("categoriaId"));
		obj.setDescricao(rs.getString("CATNAME"));
		obj.setTipoDeMovimento(null);
		obj.setIdUsuario(null);
		return obj;
	}
	
	private MeioPagamento instantiateMeioPagamento(ResultSet rs) throws SQLException {
		
		MeioPagamento obj = new MeioPagamento();
		obj.setId(rs.getInt("meiopagamentoId"));
		obj.setDescricao(rs.getString("MPNAME"));
		obj.setSaldo(rs.getDouble("MPSALDO"));
		obj.setUsuarioId(null);
		return obj;

	}

	@Override
	public void update(MovimentoFinanceiro entity) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE movimentofinanceiro "
					+ "SET descricao = ?, dataTransacao = ?, valor = ?, categoriaId = ?, meiopagamentoId = ?, usuarioId = ? "
					+ "WHERE Id = ?");
			st.setString(1, entity.getDescricao());
			st.setDate(2, new java.sql.Date(entity.getDataTransacao().getTime()));
			st.setDouble(3, entity.getValor());
			st.setInt(4, entity.getCategoria().getId());
			st.setInt(5, entity.getMeioPagamento().getId());
			st.setInt(6, entity.getId());
			
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
	public void updateSaldo(Double saldo, Integer id) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("CALL diminuiCash(?, ?)");
			st.setDouble(1, saldo);
			st.setInt(2, id);

			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

}
