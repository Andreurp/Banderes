package net.andreu.Banderes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

public class BandreresController implements Initializable {
	@FXML
	private ListView<String> lvRecortPais;
	@FXML
	private TextArea tbaTextRecort;
	@FXML
	private ComboBox<String> cbxPais;
	@FXML
	private Button btnCerca;
	@FXML
	private Label tbxNomPais;
	@FXML
	private TextArea tbaText;
	@FXML
	private Label tbxNumTrobades;

	private String arxiu = "src/main/resources/Banderes.txt";
	private List<Pais> paisos = new ArrayList<Pais>();
	private String[] colors;
	private Map<String, Integer> totalBanderesTrobades = new HashMap<String, Integer>();

	public void initialize(URL arg0, ResourceBundle arg1) {

		procesaFitxer();

		for (Pais p : paisos) {
			cbxPais.getItems().add(p.getNom());
		}
		cbxPais.setValue(paisos.get(0).getNom());

	}

	// Event Listener on Button[#btnCerca].onMouseClicked
	@FXML
	public void CercaBandera(MouseEvent event) throws IOException {
		int posicioPais = 0;
		int nBanderes = 0;
		int colorBusca = 0;
		String nomPais = cbxPais.getValue();
		tbxNomPais.setText(nomPais);

		obtenirTextColors();

		while (!nomPais.equals(paisos.get(posicioPais).getNom()) && posicioPais < paisos.size()) {
			posicioPais++;

		}
		String[] bandera = paisos.get(posicioPais).getColors();

		for (int i = 0; i < colors.length; i++) {
			if (bandera[colorBusca].equals(colors[i])) {
				colorBusca++;
				if (colorBusca >= bandera.length) {
					colorBusca = 0;
					nBanderes++;
				}
			}
		}
		tbxNumTrobades.setText("" + nBanderes);
		
		ObservableList<String> items = FXCollections.observableArrayList();
		int k=0;
		if (totalBanderesTrobades.get(nomPais) != null) {
			k = totalBanderesTrobades.get(nomPais);
			if(k < nBanderes){
				totalBanderesTrobades.put(nomPais, nBanderes);
			}
		}else{
			totalBanderesTrobades.put(nomPais, nBanderes);
		}

		for (Map.Entry<String, Integer> entry : totalBanderesTrobades.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			items.add(key + " " + value);

		}
		lvRecortPais.setItems(items);
		
		String textServidor = tbaText.getText();
		String textRecort = tbaTextRecort.getText();
		int nGran = 0;
		if(textRecort.equals("")){
			tbaTextRecort.setText(textServidor);
		}
		for(Map.Entry<String, Integer> entry : totalBanderesTrobades.entrySet()){
			int value = entry.getValue();
			if(value > nGran){
				nGran = value;
			}
		}
		if(nBanderes > nGran){
			tbaTextRecort.setText(textServidor);
		}
	}

	private void obtenirTextColors() throws IOException {

		Document doc = Jsoup.connect("http://localhost:9090/").get();
		String text = doc.select("body").text();
		tbaText.setText(text.toString());

		colors = text.split(",");
	}

	private void procesaFitxer() {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(arxiu));
			String linia;
			while ((linia = br.readLine()) != null) {
				llegirLinia(linia);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void llegirLinia(String linia) {
		String[] nomPais = linia.split(":");
		String[] colorsPais = nomPais[1].split(",");

		paisos.add(new Pais(nomPais[0].toString(), colorsPais));
	}
}
