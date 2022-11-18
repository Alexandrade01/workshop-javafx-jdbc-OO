package model.entities;

import java.io.Serializable;
import java.util.Date;

public class MovimentoFinanceiro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String descricao;
	private Date dataTransacao;
	private Double valor;
	private Categoria categoria;
	private MeioPagamento meioPagamento;
	private Integer usuario;
	
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
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public MeioPagamento getMeioPagamento() {
		return meioPagamento;
	}
	public void setMeioPagamento(MeioPagamento meioPagamento) {
		this.meioPagamento = meioPagamento;
	}
	public Integer getUsuario() {
		return usuario;
	}
	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}

	
	public MovimentoFinanceiro() {
		super();
	}
	public MovimentoFinanceiro(Integer id, String descricao, Date dataTransacao, Double valor, Categoria categoria,
			MeioPagamento meioPagamento, Integer usuario) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dataTransacao = dataTransacao;
		this.valor = valor;
		this.categoria= categoria;
		this.meioPagamento = meioPagamento;
		this.usuario = usuario;
	}

}
