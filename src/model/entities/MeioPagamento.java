package model.entities;

import java.io.Serializable;

public class MeioPagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private Double saldo;
	private Integer usuarioId;

	public MeioPagamento(Integer id, String descricao, Double saldo, Integer usuarioId) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.saldo = saldo;
		this.usuarioId = usuarioId;
	}

	public MeioPagamento() {
		// TODO Auto-generated constructor stub
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Double pagarDespesa(Double quantia) {

		return null;
	}

	public Double receberReceita(Double quantia) {

		return null;
	}

}
