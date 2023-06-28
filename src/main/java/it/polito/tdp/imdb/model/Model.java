package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;
//importante per query impegnativa su attori e direttori 
public class Model {
	ImdbDAO dao;
	Graph<Director, DefaultWeightedEdge> grafo;
	List<Arco> archi;
	Map<Integer, Director> mappaDirector;
	public Model() {
		dao=new ImdbDAO();
		mappaDirector= new HashMap<>();
		this.dao.listAllDirectors(mappaDirector);
	}
	
	public void buildGraph(int year) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		archi=new ArrayList<>();
		Graphs.addAllVertices(grafo, dao.gettVertici(year));
		archi= this.dao.getAttoriPerDirettore(mappaDirector, year);
		for(Arco a: archi) {
			Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
		}
	}
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	public Set<Director> getVertici(){
		return this.grafo.vertexSet();
	}
	public List<Arco> getAdiacenti(Director d){
		List<Director> adiacenti=Graphs.neighborListOf(grafo, d);
		List<Arco> listaArchi= new ArrayList<>();
		for(Director d2: adiacenti){
		DefaultWeightedEdge arco=grafo.getEdge(d, d2);
		int peso=(int) grafo.getEdgeWeight(arco);
		Arco arco2= new Arco(d, d2, peso);
		listaArchi.add(arco2);
	}
		Collections.sort(listaArchi);
		return listaArchi;
	}
}
