package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private List<String> ruoli;
	private List<Artist> vertici;
	private Map<Integer, Artist> artisti;
	private List<Adiacenza> adiacenze;
	//Strutture dati per la ricorsione
	private List<Artist> best;
	private int numExpoCondivise;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.ruoli = new ArrayList<>();
	}
	
	public List<String> getRuoli(){
		this.ruoli = this.dao.getRoles();
		return this.ruoli;
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		this.artisti = new HashMap<>();
		this.vertici = new ArrayList<>(this.dao.getAllArtists(ruolo, this.artisti));
		Graphs.addAllVertices(this.grafo, this.vertici);
		//aggiungi gli archi
		this.adiacenze = new ArrayList<>(this.dao.getArchi(ruolo, artisti));
		for(Adiacenza a : this.adiacenze) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public boolean isGraphCreated() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	public int nVertices() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public Map<Integer, Artist> getArtisti(){
		return this.artisti;
	}
	
	public List<Adiacenza> getArtistiConnessi(){
		List<Adiacenza> ad = new ArrayList<>(this.adiacenze);
		Collections.sort(ad);
		return ad;
	}
	
	public List<Artist> cercaCammino(Artist partenza){
		List<Artist> parziale = new ArrayList<>();
		parziale.add(partenza);
		this.best = new ArrayList<>();
		ricerca(parziale, -1);
		return this.best;
	}

	private void ricerca(List<Artist> parziale, int peso) {
		if(parziale.size() > this.best.size()) {
			this.best = new ArrayList<>(parziale);
		}
		Artist ultimo = parziale.get(parziale.size()-1);
		//prendo i vicini dell'ultimo artista
		List<Artist> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		for(Artist a : vicini) {
			if(!parziale.contains(a) && peso == -1) {
				parziale.add(a);
				ricerca(parziale, (int)this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a)));
				parziale.remove(a);
			}else {
				if(!parziale.contains(a) && (int)this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a)) == peso) {
					parziale.add(a);
					ricerca(parziale, peso);
					parziale.remove(a);
				}
			}
		}
	}
}
