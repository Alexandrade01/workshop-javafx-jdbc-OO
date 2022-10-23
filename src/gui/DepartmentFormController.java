package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import model.entities.Department;
import model.exceptions.ValidationException;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;

	private DepartmentService service;

	// lista de componentes interessados em alterações
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setDepartment(Department entity) {

		this.entity = entity;

	}

	public void setDepartmentService(DepartmentService service) {

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

		} 
		catch(ValidationException e) {
			
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

	private Department getFormData() {

		Department obj = new Department();

		ValidationException validationException = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {

			validationException.addError("name", "O campo não pode ser vazio ! ");

		}
		obj.setName(txtName.getText());

		if (validationException.getErrors().size() > 0) {

			throw validationException;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
		System.out.println("onBtCancelAction");

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);

	}

	// popular formulario com o objeto
	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	
	private void setErrorMessages(Map<String,String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			
			labelErrorName.setText(errors.get("name"));
		}
	}

}
