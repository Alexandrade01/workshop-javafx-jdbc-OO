package model.dao;

import model.entities.Usuario;

public interface UsuarioDao {

	void insert(Usuario obj);

	Usuario findByEmailSenha(String user, String senha);

	Usuario findByEmail(String email);

}
