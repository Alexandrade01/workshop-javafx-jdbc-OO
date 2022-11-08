package model.entities;

import model.enumerations.TipoDeMovimento;

public class Categoria {
	
	private Integer id;
	private String descricao;
	private TipoDeMovimento tipoDeMovimento;
	private Integer idUsuario;
	
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
	public TipoDeMovimento getTipoDeMovimento() {
		return tipoDeMovimento;
	}
	public void setTipoDeMovimento(TipoDeMovimento tipoDeMovimento) {
		this.tipoDeMovimento = tipoDeMovimento;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public Categoria(Integer id, String descricao, TipoDeMovimento tipoDeMovimento, Integer idUsuario) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.tipoDeMovimento = tipoDeMovimento;
		this.idUsuario = idUsuario;
	}
	
	public Categoria() {
		super();
	}

}
