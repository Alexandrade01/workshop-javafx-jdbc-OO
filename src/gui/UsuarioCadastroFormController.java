package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.util.Alerts;
import gui.util.Utils;
import gui.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Usuario;
import model.exceptions.ValidationException;
import model.service.UsuarioService;

public class UsuarioCadastroFormController implements Initializable {

	private Usuario entity;

	private UsuarioService usuarioService;
	
	private ValidationException validationException;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtSobrenome;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtSenha;
	
	@FXML
	private TextField txtRepeticaoSenha;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btHelpSenha;

	@FXML
	private Button btCancel;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorSobrenome;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorSenha;
	
	@FXML
	private Label labelErrorRepeticaoSenha;

	public void setServices(UsuarioService service) {

		usuarioService = service;
	}

	public void setUsuario(Usuario user) {

		entity = user;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {

		try {
			usuarioService = new UsuarioService();
			entity = new Usuario();
			entity = getFormData();
			if(usuarioService.findUserByEmail(txtEmail.getText())  != null) {
				
				Alerts.showAlert("Email já cadastrado !", "Falha no cadastro ! ", "Error", AlertType.ERROR);
			}
			else {
				
				usuarioService.saveOrUpdate(entity);
				clearFields();
				clearErrors();
				Alerts.showAlert("Cadastro feito", "Sucesso ! ", null, AlertType.INFORMATION);
			}

		} catch (ValidationException e) {

			setErrorMessages(e.getErrors());
		}

		catch (DbException e) {

			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtHelpSenhaAction(ActionEvent event) {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/HelpSenha.fxml"));

		try {
			Pane pane = loader.load();
			
			Scene mainScene = new Scene(pane);
			
			Stage primaryStage = new Stage();
			primaryStage.setScene(mainScene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Fesa Wallet");
			primaryStage.initModality(Modality.APPLICATION_MODAL);
			primaryStage.show();
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

	private Usuario getFormData() {

		Usuario obj = new Usuario();
		
		validationException = new ValidationException("Validation error");

		// Validacao nome
		if (txtNome.getText() == null || txtNome.getText().trim().equals("") || !ValidationUtil.validacaoTamanho(txtNome.getText())) {
			validationException.addError("nome", "O nome esta invalido !");
		}
		obj.setNome(txtNome.getText());

		// Validacao sobrenome
		if (txtSobrenome.getText() == null || txtSobrenome.getText().trim().equals("") ||  !ValidationUtil.validacaoTamanho(txtSobrenome.getText())) {
			validationException.addError("sobrenome", "O sobrenome esta invalido !");
		}
		obj.setSobrenome(txtSobrenome.getText());

		// Validacao Email
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("") || !ValidationUtil.validacaoEmail(txtEmail.getText())) {
			validationException.addError("email", "O email esta invalido !");
		}
		obj.setEmail(txtEmail.getText());

		// Validacao senha
		if (txtSenha.getText() == null || txtSenha.getText().trim().equals("") || !ValidationUtil.validacaoSenha(txtSenha.getText())) {
			validationException.addError("senha", "A senha esta invalida !");
		}
		
		if(!txtSenha.getText().equals(txtRepeticaoSenha.getText())){ 
			
			validationException.addError("senhaRepetida", "As senhas estão incompativeis !");
			
		}
		
		obj.setSenha(txtSenha.getText());

		if (validationException.getErrors().size() > 0) {

			throw validationException;
		}

		return obj;
	}

	private void clearFields() {

		txtNome.clear();
		txtSobrenome.clear();
		txtEmail.clear();
		txtSenha.clear();
		txtRepeticaoSenha.clear();
	}
	
	private void clearErrors() {
		
		labelErrorNome.setText("");
		labelErrorSobrenome.setText("");
		labelErrorEmail.setText("");
		labelErrorSenha.setText("");
		labelErrorRepeticaoSenha.setText("");


	}


	@FXML
	public void onBtCancelAction(ActionEvent event) {

		Utils.currentStage(event).close();

	}
	
	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();
		
		labelErrorNome.setText(fields.contains("nome") ? errors.get("nome") : "");
		labelErrorSobrenome.setText(fields.contains("sobrenome") ? errors.get("sobrenome") : "");
		labelErrorEmail.setText(fields.contains("email") ? errors.get("email") : "");
		labelErrorSenha.setText(fields.contains("senha") ? errors.get("senha") : "");
		labelErrorRepeticaoSenha.setText(fields.contains("senhaRepetida") ? errors.get("senhaRepetida") : "");
		
	}

}
