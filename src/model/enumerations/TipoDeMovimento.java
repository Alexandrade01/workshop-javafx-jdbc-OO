package model.enumerations;

public enum TipoDeMovimento {
	
	RECEITA(1),
	DESPESA(2);
	
	public int valor;
	TipoDeMovimento(int valor) {
		 this.valor = valor;
	}
	
	

}
