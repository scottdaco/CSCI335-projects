package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        Histogram<L> hl = new Histogram<>();
        ArrayList<Duple<Double,L> > ll = new ArrayList<>();
        double d = 0;
        for(int i = 0; i < data.size(); i++) {
            d = distance.applyAsDouble(value, data.get(i).getFirst());
            Duple<Double, L> l = new Duple<>(d, data.get(i).getSecond());
            System.out.println(l.toString());
            ll.add(l);
        }
        while(ll.size() != 3) {
            Double v = 0.0;
            int vv = 0;
            for (int i = 0; i < ll.size(); i++) {
                if(ll.get(i).getFirst() > v) {
                    v = ll.get(i).getFirst();
                    vv = i;
                }
            }
            ll.remove(vv);
        }
        for(int i = 0; i < ll.size(); i++) {
            hl.bump(ll.get(i).getSecond());
        }
        return hl.getPluralityWinner();
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        data.addAll(training);
        // TODO: Add all elements of training to data.
    }
}
