import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe singoletto che realizza un calcolatore delle componenti connesse di un
 * grafo non orientato utilizzando una struttura dati efficiente (fornita dalla
 * classe {@ForestDisjointSets<GraphNode<L>>}) per gestire insiemi disgiunti di
 * nodi del grafo che sono, alla fine del calcolo, le componenti connesse.    
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del grafo
 */
public class UndirectedGraphConnectedComponentsComputer<L> {

    /*
     * Struttura dati per gli insiemi disgiunti.
     */
    private ForestDisjointSets<GraphNode<L>> f;

    /**
     * Crea un calcolatore di componenti connesse.
     */

    public UndirectedGraphConnectedComponentsComputer() {
        this.f = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Calcola le componenti connesse di un grafo non orientato utilizzando una
     * collezione di insiemi disgiunti.
     * 
     * @param g
     *              un grafo non orientato
     * @return un insieme di componenti connesse, ognuna rappresentata da un
     *         insieme di nodi del grafo
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * @throws IllegalArgumentException
     *                                      se il grafo passato è orientato
     */
    public Set<Set<GraphNode<L>>> computeConnectedComponents(Graph<L> g) {
        if (g == null) {
            throw new NullPointerException("Il grafo passato non può essere nullo.");
        }
        if (g.isDirected()) {
            throw new IllegalArgumentException("Il grafo passato deve essere non orientato.");
        }
        f.clear();
        for (GraphNode<L> node : g.getNodes()) {
            f.makeSet(node);
        }
        for (GraphEdge<L> edge : g.getEdges()) {
            if (!edge.isDirected()) {
                f.union(edge.getNode1(), edge.getNode2());
            }
        }
        Map<GraphNode<L>, Set<GraphNode<L>>> componentsMap = new HashMap<>();
        for (GraphNode<L> node : g.getNodes()) {
            GraphNode<L> representative = f.findSet(node);
            componentsMap.computeIfAbsent(representative, k -> new HashSet<>()).add(node);
        }

        return new HashSet<>(componentsMap.values());
    }
}
