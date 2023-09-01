package learning.markov;

import learning.core.Histogram;

import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    public void count(Optional<S> prev, L label, S next) {
        if (label2symbol2symbol.isEmpty() || !label2symbol2symbol.containsKey(label)) {
            HashMap<Optional<S>, Histogram<S>> a = new HashMap<>();
            label2symbol2symbol.put(label, a);
        }
        if (label2symbol2symbol.get(label).get(prev) == null) {
            Histogram<S> b = new Histogram<>();
            label2symbol2symbol.get(label).put(prev, b);
        }
        if (label2symbol2symbol.get(label).get(prev) != null) {
            label2symbol2symbol.get(label).get(prev).bump(next);
        }
    }

    public double probability(ArrayList<S> sequence, L label) {
        ArrayList<Optional<S>> a = oc(sequence);
        Iterator<Optional<S>> k = a.iterator();
        Iterator<S> y = sequence.iterator();
        if(y.hasNext()) {
            y.next();
        }
        if(!a.isEmpty()) {
            count(a.get(0), label, sequence.get(0));
        }
        double n = 1;
        for(int i=0; i < sequence.size(); i++) {
            if (k.hasNext()) {
                Optional<S> p = k.next();
                if (y.hasNext()) {
                    S r = y.next();
                    if(label2symbol2symbol.get(label).get(p) != null) {
                        n *= ((double) ((label2symbol2symbol.get(label).get(p)).getCountFor(r))) / ((double) ((label2symbol2symbol.get(label).get(p)).getTotalCounts()));
                    }
                }
            }
        }
        return (n);
    }
    public ArrayList<Optional<S>> oc(ArrayList<S> sequence) {
        ArrayList<Optional<S>> a = new ArrayList<>();
        for(int i = 0; i < sequence.size()-1; i++) {
            Optional<S> v = Optional.of(sequence.get(i));
            a.add(v);
        }
        return a;
    }

    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        LinkedHashMap<L,Double> a = new LinkedHashMap<>();
        Iterator<L> b = label2symbol2symbol.keySet().iterator();
        for(int i = 0; i < label2symbol2symbol.size(); i++) {
            L g = b.next();
            a.put(g, probability(sequence, g));
        }
        b = label2symbol2symbol.keySet().iterator();
        L g = null;
        double k = 0;
        for(int i = 0; i < label2symbol2symbol.size(); i++) {
            g = b.next();
            k += a.get(g);
        }
        b = label2symbol2symbol.keySet().iterator();
        for(int i = 0; i < label2symbol2symbol.size(); i++) {
            g = b.next();
            a.put(g, a.get(g)/k);
        }
        return a;
    }


    public L bestMatchingChain(ArrayList<S> sequence) {
        LinkedHashMap<L,Double> a = labelDistribution(sequence);
        Iterator<L> b = a.keySet().iterator();
        L prevl = null;
        L d = b.next();
        L highest = null;
        for(int i = 0; i < a.size()-1; i++) {
                if(i==0) {
                    prevl = d;
                    d = b.next();
                    highest = prevl;
                }
                else {
                    if(a.get(highest) < a.get(prevl)) {
                        highest = prevl;
                    } else if (a.get(highest) < a.get(d)) {
                        highest = d;
                    }
                }
                }

        return highest;
        }




    }



