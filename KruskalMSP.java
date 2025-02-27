package it.unicam.cs.asdl2425.mp2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) 
 * 			**JULIANO, SINAJ juliano.sinaj@studenti.unicam.it **
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMSP<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;
    private List<GraphEdge<L>> edges;
    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMSP() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
        this.edges = new ArrayList<>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */

    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        if (g == null) {
            throw new NullPointerException("Graph cannot be null.");
        }

        if (g.isDirected()) {
            throw new IllegalArgumentException("Graph must be undirected.");
        }

        if (g.getEdges().isEmpty()) {
            return new HashSet<>();
        }

        for (GraphEdge<L> edge : g.getEdges()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Graph cannot have negative edge weights.");
            }
        }

        this.edges.clear();
        this.edges.addAll(g.getEdges());

        Set<GraphEdge<L>> mst = new HashSet<>();
        disjointSets.clear();

        for (GraphNode<L> node : g.getNodes()) {
            disjointSets.makeSet(node);
        }

        this.edges.sort(Comparator.comparingDouble(GraphEdge::getWeight));

        for (GraphEdge<L> edge : this.edges) {
            GraphNode<L> u = edge.getNode1();
            GraphNode<L> v = edge.getNode2();

            if (disjointSets.findSet(u) != disjointSets.findSet(v)) {
                mst.add(edge);
                disjointSets.union(u, v);

                if (mst.size() == g.getNodes().size() - 1) {
                    return mst;
                }
            }
        }
        if (!mst.isEmpty()) return mst;
            return new HashSet<>();
        }
    }