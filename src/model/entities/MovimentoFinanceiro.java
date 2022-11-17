package model.entities;

import java.io.Serializable;
import java.util.Date;

public class MovimentoFinanceiro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String descricao;
	private Date dataTransacao;
	private Double valor;
	private Integer categoriaId;
	private Integer meioPagamentoId;
	private Integer usuarioId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataTransacao() {
		return dataTransacao;
	}
	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Integer getCategoriaId() {
		return categoriaId;
	}
	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}
	public Integer getMeioPagamentoId() {
		return meioPagamentoId;
	}
	public void setMeioPagamentoId(Integer meioPagamentoId) {
		this.meioPagamentoId = meioPagamentoId;
	}
	public Integer getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public MovimentoFinanceiro(Integer id, String descricao, Date dataTransacao, Double valor, Integer categoriaId,
			Integer meioPagamentoId, Integer usuarioId) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dataTransacao = dataTransacao;
		this.valor = valor;
		this.categoriaId = categoriaId;
		this.meioPagamentoId = meioPagamentoId;
		this.usuarioId = usuarioId;
	}
	
	public MovimentoFinanceiro() {
		super();
	}

}
