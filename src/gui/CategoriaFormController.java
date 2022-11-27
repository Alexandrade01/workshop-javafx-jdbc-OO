package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Categoria;
import model.enumerations.TipoDeMovimento;
import model.exceptions.ValidationException;
import model.service.CategoriaService;

public class CategoriaFormController implements Initializable {

	private Categoria entity;

	private CategoriaService service;

	private Integer usuarioId;

	private ObservableList<TipoDeMovimento> obsList;

	// lista de componentes interessados em alterações
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtDescricao;

	@FXML
	private TextField txtIdUsuario;

	@FXML
	private ComboBox<TipoDeMovimento> comboBoxTipoDeMovimento;

	@FXML
	private Label labelErrorDescricao;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setCategoria(Categoria entity) {

		this.entity = entity;

	}

	public void setServices(CategoriaService service) {

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

	private Categoria getFormData() {

		Categoria obj = new Categoria();

		ValidationException validationException = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		// Validacao descricao
		if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
			validationException.addError("descricao", "O campo não pode ser vazio ! ");
		}
		obj.setDescricao(txtDescricao.getText());

		// validacao department
		obj.setTipoDeMovimento(TipoDeMovimento.valueOf(comboBoxTipoDeMovimento.getValue().toString()));

		if (validationException.getErrors().size() > 0) {

			throw validationException;
		}

		obj.setIdUsuario(usuarioId);

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

		usuarioId = Main.getUsuarioID();

		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtDescricao, 70);
		Constraints.setTextFieldInteger(txtIdUsuario);
		initializeComboBoxTipoDeMovimento();

	}

	// popular formulario com o objeto
	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtDescricao.setText(entity.getDescricao());
		txtIdUsuario.setText(String.valueOf(entity.getIdUsuario()));

		if (entity.getTipoDeMovimento() == null) {

			comboBoxTipoDeMovimento.getSelectionModel().selectFirst();
		} else {
			comboBoxTipoDeMovimento.setValue(entity.getTipoDeMovimento());
		}

	}

	public void loadAssociatedObjects() {
		obsList = FXCollections.observableArrayList(TipoDeMovimento.values());
		comboBoxTipoDeMovimento.setItems(obsList);

	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		labelErrorDescricao.setText(fields.contains("descricao") ? errors.get("name") : "");

	}

	private void initializeComboBoxTipoDeMovimento() {
		Callback<ListView<TipoDeMovimento>, ListCell<TipoDeMovimento>> factory = lv -> new ListCell<TipoDeMovimento>() {
			@Override
			protected void updateItem(TipoDeMovimento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.toString());
			}
		};
		comboBoxTipoDeMovimento.setCellFactory(factory);
		comboBoxTipoDeMovimento.setButtonCell(factory.call(null));
	}

}
