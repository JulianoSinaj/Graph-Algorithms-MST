import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }
    }
    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        this.currentElements = new HashMap<E, Node<E>>();
    }

    @Override
    public boolean isPresent(E e) {
        if (currentElements.containsKey(e)) return true;
            return false;
        }
    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        if (e == null) throw new NullPointerException("Elemento nullo non ammesso.");
        if (this.isPresent(e)) throw new IllegalArgumentException("Elemento già presente nella struttura.");
        Node<E> newNode = new Node<>(e);
        this.currentElements.put(e, newNode);
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        if (e == null) {
            throw new NullPointerException("Elemento nullo non ammesso.");
        }
        if (!currentElements.containsKey(e)) {
            throw new IllegalArgumentException("Elemento non presente nella struttura.");
        }
        Node<E> node = currentElements.get(e);
        if (node != null) {
            if (node != node.parent) {
                node.parent = currentElements.get(findSet(node.parent.item));
            }
            return node.parent.item;
        }
        return null;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        if (e1 == null || e2 == null) {
            throw new NullPointerException("Elements cannot be null");
        }
        if (!currentElements.containsKey(e1) || !currentElements.containsKey(e2)) {
            throw new IllegalArgumentException("One or both elements are not in the sets");
        }
        Node<E> root1 = currentElements.get(findSet(e1));
        Node<E> root2 = currentElements.get(findSet(e2));

        if (root1 != root2) {
            if (root1.rank < root2.rank) {
                root1.parent = root2;
            } else if (root1.rank > root2.rank) {
                root2.parent = root1;
            } else {
                root1.parent = root2;
                root2.rank++;
            }
          }
       }

    @Override
    public Set<E> getCurrentRepresentatives() {
        Set<E> representatives = new HashSet<>();
        for (E element : currentElements.keySet()) {
            representatives.add(findSet(element)); 
         }
        return representatives;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if (e == null) {
            throw new NullPointerException("Elemento nullo non ammesso.");
        }
        if (!this.isPresent(e)) {
            throw new IllegalArgumentException("Elemento non presente nella struttura.");
        }
        Set<E> elements = new HashSet<>();
        E representative = findSet(e);
        for (Map.Entry<E, Node<E>> entry : this.currentElements.entrySet()) {
            if (findSet(entry.getKey()).equals(representative)) {
                elements.add(entry.getKey());
            }
        }
        if (!elements.isEmpty()) return elements;
            return null; 
    }

    @Override
    public void clear() {
        this.currentElements.clear();
    }
}
