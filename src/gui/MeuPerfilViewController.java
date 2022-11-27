package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import model.service.MeioPagamentoService;

public class MeuPerfilViewController implements Initializable {

	@FXML
	private Label textoNome;
	@FXML
	private PieChart chartMinhasCarteiras;
	@FXML
	private Label textSaldo;

	Double totalreceita = Double.valueOf(0);
	Double totaldespesa = Double.valueOf(0);
	Double meuSaldo = Double.valueOf(0);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		textoNome.setText(Main.getUsuarioName().toUpperCase());

		MeioPagamentoService service = new MeioPagamentoService();
		totalreceita = service.totalReceitasById(Main.getUsuarioID());
		totaldespesa = service.totalDespesasById(Main.getUsuarioID());

		meuSaldo = service.meuSaldoById(Main.getUsuarioID());
		textSaldo.setText("R$ " + String.format("%.2f", meuSaldo));

		createPieChart();

	}

	private void createPieChart() {

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("RECEITAS R$" + String.format("%.2f", totalreceita), totalreceita),
				new PieChart.Data("DESPESAS R$" + String.format("%.2f", totaldespesa), totaldespesa));
		chartMinhasCarteiras.setData(pieChartData);

	}

}
