package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	for(Adiacenza a : this.model.getArtistiConnessi()) {
    		this.txtResult.appendText(a.getA1()+" - "+a.getA2()+ ". Peso : "+a.getPeso()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	int id;
    	Artist a = null;
    	try {
    		id = Integer.parseInt(this.txtArtista.getText());
    		a = this.model.getArtisti().get(id);
    		if(a == null) {
    			this.txtResult.setText("Hai inserito un identificativo non valido!");
    			return;
    		}
    	}catch(NumberFormatException e ) {
    		this.txtResult.setText("Devi inserire un valore numerico intero");
    	}
    	//se sono qui posso proseguire con la ricorsione
    	List<Artist> migliore = new ArrayList<>(this.model.cercaCammino(a));
    	for(Artist aa : migliore) {
    		this.txtResult.appendText(""+ aa.getName()+ " - "+ aa.getArtistId()+"\n");
    	}
    	this.txtResult.appendText("Numero di esposizioni per il percorso massimo: "+migliore.size()+"\n");
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	//controlla input
    	String role = this.boxRuolo.getValue();
    	if(role == null) {
    		this.txtResult.setText("Devi selezionare un ruolo dall'apposita tendina");
    		return;
    	}
    	//se sono qui posso creare il grafo
    	this.model.creaGrafo(role);
    	this.txtResult.setText("Grafo creato\n");
    	this.txtResult.appendText("#VERTICI: "+this.model.nVertices()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().clear();
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
