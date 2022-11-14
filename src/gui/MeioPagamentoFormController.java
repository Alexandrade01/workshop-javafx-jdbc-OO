package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.MeioPagamento;
import model.exceptions.ValidationException;
import model.service.MeioPagamentoService;

public class MeioPagamentoFormController implements Initializable {

	private MeioPagamento entity;

	private MeioPagamentoService service;

	// lista de componentes interessados em alterações
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private TextField txtSaldo;

	@FXML
	private TextField txtUsuarioId;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setMeioPagamento(MeioPagamento entity) {

		this.entity = entity;

	}

	public void setServices(MeioPagamentoService service) {

		this.service = service;

	}
	
	// injecao da lista
	public void subscribeDataChangeListener(DataChangeListener listener) {

		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		// controle de aviso para o programador injetar a dependencia
		if (entity == null) {
			throw new IllegalStateException();
		}
		if (service == null) {
			throw new IllegalStateException();
		}
		try {

			entity = getFormData();
			service.saveOrUpdate(entity);
			// possuindo algum item na lista de dataChangeListeners ele ira atualizar a
			// lista do formulario
			notifyDataChangeListener();
			Utils.currentStage(event).close();

		} catch (ValidationException e) {

			setErrorMessages(e.getErrors());
		}

		catch (DbException e) {

			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void notifyDataChangeListener() {

		for (DataChangeListener listener : dataChangeListeners) {

			listener.onDataChanged();

		}

	}

	private MeioPagamento getFormData() {

		MeioPagamento obj = new MeioPagamento();

		ValidationException validationException = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		// Validacao descricao
		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			validationException.addError("descricao", "O campo não pode ser vazio ! ");
		}
		obj.setDescricao(txtDescricao.getText());
		
		if(txtSaldo.getText() == null || Double.valueOf(txtSaldo.getText()) <= 0 ) {
			
			validationException.addError("saldo", "saldo invalido ! ");
			
		}
		
		obj.setSaldo(Utils.tryParseToDouble(txtSaldo.getText()));
		
		if (validationException.getErrors().size() > 0) {

			throw validationException;
		}
		
		obj.setUsuarioId(1);

		return obj;
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtDescricao, 70);
		Constraints.setTextFieldDouble(txtSaldo);
		Constraints.setTextFieldInteger(txtUsuarioId);

	}
	
	// popular formulario com o objeto
	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtDescricao.setText(entity.getDescricao());

		Locale.setDefault(Locale.US);
		txtSaldo.setText(String.format("%.2f", entity.getSaldo()));
		txtUsuarioId.setText(String.valueOf(entity.getUsuarioId()));
	}
	
	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();
		
		labelErrorDescricao.setText(fields.contains("descricao") ? errors.get("name"):"");
	}
	

}
