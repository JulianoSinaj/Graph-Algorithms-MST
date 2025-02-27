import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    // Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
    // matrice di adiacenza
    protected Map<GraphNode<L>, Integer> nodesIndex;

    // Matrice di adiacenza, gli elementi sono null o oggetti della classe
    // GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
    // dimensione gradualmente ad ogni inserimento di un nuovo nodo.
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        int count = 0;
        for (ArrayList<GraphEdge<L>> row : matrix) {
            for (GraphEdge<L> edge : row) {
                if (edge != null) {
                    count++;
                }
            }
        }
        return count / 2; 
    }

    @Override
    public void clear() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        Set<GraphNode<L>> result = new HashSet<>();
        result.addAll(nodesIndex.keySet());
        return result; 
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        if (!nodesIndex.containsKey(node)) {
            nodesIndex.put(node, nodesIndex.size());
            for (ArrayList<GraphEdge<L>> row : matrix) {
                row.add(null);
            }
            ArrayList<GraphEdge<L>> newRow = new ArrayList<>(nodesIndex.size());
            for (int i = 0; i < nodesIndex.size(); i++) {
                newRow.add(null);
            }
            matrix.add(newRow);
            return true; 
        }
        return false;
    }


    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        if (nodesIndex.containsKey(node)) {
            int indexToRemove = nodesIndex.get(node);
            matrix.remove(indexToRemove);
            for (ArrayList<GraphEdge<L>> row : matrix) {
                row.remove(indexToRemove);
            }
            Map<GraphNode<L>, Integer> updatedIndices = new HashMap<>();
            for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
                int currentIndex = entry.getValue();
                if (currentIndex > indexToRemove) {
                    updatedIndices.put(entry.getKey(), currentIndex - 1);
                } else {
                    updatedIndices.put(entry.getKey(), currentIndex);
                }
            }
            nodesIndex = updatedIndices;
            nodesIndex.remove(node);
            return true;
        }
        return false;
    }


    @Override
    public boolean containsNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        if (nodesIndex.containsKey(node)) return true;
        return false;
    }


    @Override
    public GraphNode<L> getNodeOf(L label) {
        if (label == null) {
            throw new NullPointerException("Label cannot be null");
        }
        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (node.getLabel().equals(label)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) {
            throw new NullPointerException("Label cannot be null");
        }
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getKey().getLabel().equals(label)) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("Node with the given label does not exist");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int index) {
        if (index < 0 || index >= nodesIndex.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds: " + index);
        }
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Node does not exist in the graph");
        }
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        int nodeIndex = nodesIndex.get(node);
        for (int i = 0; i < matrix.get(nodeIndex).size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) {
                adjacentNodes.add(getNodeAtIndex(i));
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> edges = new HashSet<>();
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) { // Ensure each edge is only considered once
                GraphEdge<L> edge = matrix.get(i).get(j);
                if (edge != null) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("Edge cannot be null");
        }
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("One or both nodes are not in the graph");
        }
        if (edge.isDirected()) {
            throw new IllegalArgumentException("Edge is directed, but the graph is undirected");
        }
        int index1 = nodesIndex.get(edge.getNode1());
        int index2 = nodesIndex.get(edge.getNode2());
        if (matrix.get(index1).get(index2) != null) { 
            return false;
        }
        matrix.get(index1).set(index2, edge);
        matrix.get(index2).set(index1, edge);
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("Edge cannot be null");
        }
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2())) {
            return false;
        }
        int index1 = nodesIndex.get(edge.getNode1());
        int index2 = nodesIndex.get(edge.getNode2());
        if (matrix.get(index1).get(index2) == null) { 
            return false;
        }
        matrix.get(index1).set(index2, null);
        matrix.get(index2).set(index1, null);
        return true;
    }
    
    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("Edge cannot be null");
        }
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("One or both nodes are not in the graph");
        }
        int index1 = nodesIndex.get(edge.getNode1());
        int index2 = nodesIndex.get(edge.getNode2());

        if (matrix.get(index1).get(index2) != null) return true;
        return false;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Node does not exist in the graph");
        }
        Set<GraphEdge<L>> edges = new HashSet<>();
        int nodeIndex = nodesIndex.get(node);
        for (GraphEdge<L> edge : matrix.get(nodeIndex)) {
            if (edge != null) {
                edges.add(edge);
            }
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }
}
