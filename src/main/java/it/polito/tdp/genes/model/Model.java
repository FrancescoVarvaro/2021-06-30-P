package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private Graph<String, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private Map<Integer, String> idMap;
	private List<String> migliore;
	private int pesoCammino;
	
	public Model() {
		dao = new GenesDao();
		idMap = new HashMap<Integer, String>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getAllLocalization());
		for(Coppie c : dao.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, c.getL1(), c.getL2(), dao.getPeso(c.getL1(), c.getL2()));
		}
	}
	
	public int nVertici(){
		return this.grafo.vertexSet().size();
	}
	
	public int nEdge(){
		return this.grafo.edgeSet().size();
	}
	
	public List <String> getVertici(){
		return dao.getAllLocalization();
	}
	
	public List<String> getLocalizzazioniConnesse(String Localiz){
		List <String> listaLocConnesse = Graphs.neighborListOf(this.grafo, Localiz);
		List <String> locPesate = new ArrayList<>();
		for(String s : listaLocConnesse) {
			locPesate.add(s+" peso:"+this.grafo.getEdgeWeight(this.grafo.getEdge(Localiz, s)));
		}
		return locPesate;
	}
	
	public List<String> getSequenza(String partenza){
		List<String> parziale = null;
		migliore = new ArrayList<>();
		pesoCammino = 0;
		ricorsione(parziale, 0, partenza);
		return migliore;
	}

	private void ricorsione(List<String> parziale, int livello, String partenza) {
		//terminazione
		int lunghezza = calcolaCammino(parziale);
		if(lunghezza>pesoCammino){
			//possibile sol.
			pesoCammino = lunghezza;
			migliore = new ArrayList<>(parziale);
			}
		for(String s : Graphs.neighborListOf(grafo, partenza)) {
			if(!parziale.contains(s))//devo verificare che quello che sto per aggiungere non sia gia presente in parziale
				parziale.add(s);
				ricorsione(parziale, livello+1, partenza);
				parziale.remove(parziale.size()-1);
		}
	}
	private int calcolaCammino(List<String> parziale) {
		int somma = 0;
		for(int i=0; i<parziale.size()-1;i++) {
			String p = parziale.get(i);//vertice
			String a =parziale.get(i+1);// il successivo
			int peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(p, a));
			somma += peso;
		}
		return somma;
	}
	
	
}