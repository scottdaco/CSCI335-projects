package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // TODO: Implement this class
    // HINT: Use java.util.PriorityQueue. It will really help you.

    private ToIntFunction<T> h;
//    private PriorityQueue<SearchNode<T>> q = new PriorityQueue<>();
    private PriorityQueue<SearchNode<T>> q = new PriorityQueue<>(new Comparator<SearchNode<T>>() {
        @Override
        public int compare(SearchNode<T> o1, SearchNode<T> o2) {
            int oo1 = h.applyAsInt(o1.getValue()) + o1.getDepth();
            int oo2 = h.applyAsInt(o2.getValue()) + o2.getDepth();
            return oo1 - oo2;
        }
    });

    private HashMap<T, Integer> v = new HashMap<>();



    public BestFirstQueue(ToIntFunction<T> heuristic) {
        h = heuristic;
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        int est = h.applyAsInt(node.getValue()) + node.getDepth();
        if (!v.containsKey(node.getValue()) || v.get(node.getValue()) > est) {
            q.add(node);
            v.put(node.getValue(), est);
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        if (q.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(q.remove());
        }
    }
}
