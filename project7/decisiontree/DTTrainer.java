package learning.decisiontree;

import core.Duple;
import learning.core.Histogram;
import learning.handwriting.core.Drawing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTTrainer<V,L, F, FV extends Comparable<FV>> {
	private ArrayList<Duple<V,L>> baseData;
	private boolean restrictFeatures;
	private Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures;
	private BiFunction<V,F,FV> getFeatureValue;
	private Function<FV,FV> successor;
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 boolean restrictFeatures, BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		baseData = data;
		this.restrictFeatures = restrictFeatures;
		this.allFeatures = allFeatures;
		this.getFeatureValue = getFeatureValue;
		this.successor = successor;
	}
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		this(data, allFeatures, false, getFeatureValue, successor);
	}

	public static <V,L, F, FV  extends Comparable<FV>> ArrayList<Duple<F,FV>>
	reducedFeatures(ArrayList<Duple<V,L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures, int targetNumber) {
		ArrayList<Duple<F,FV>> a = allFeatures.apply(data);
		Collections.shuffle(a);
		ArrayList<Duple<F,FV>> b = new ArrayList<>();
		for(int i = 0; i < targetNumber; i++) {
			b.add(a.get(i));
		}
		return b;
    }
	
	public DecisionTree<V,L,F,FV> train() {
		return train(baseData);
	}

	public static <V,L> int numLabels(ArrayList<Duple<V,L>> data) {
		return data.stream().map(Duple::getSecond).collect(Collectors.toUnmodifiableSet()).size();
	}
	
	private DecisionTree<V,L,F,FV> train(ArrayList<Duple<V,L>> data) {
		DTInterior<V,L,F,FV> t = null;
		if (numLabels(data) == 1) {
			return new DTLeaf<>(data.get(0).getSecond());
		} else {
			if(!restrictFeatures) {
				allFeatures.apply(data);
			}
			else {
				reducedFeatures(data, allFeatures, data.size());
			}
			double hfc = 0;
			Duple<ArrayList<Duple<V,L>>, ArrayList<Duple<V,L>>> hs = splitOn(data, allFeatures.apply(data).get(0).getFirst(), allFeatures.apply(data).get(0).getSecond(), getFeatureValue);
			FV hfv = allFeatures.apply(data).get(0).getSecond();
			F df = allFeatures.apply(data).get(0).getFirst();
			for(Duple<F,FV> a: allFeatures.apply(data)) {
					Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> spd3 = splitOn(data, a.getFirst(), a.getSecond(), getFeatureValue);
					double b3 = gain(data, spd3.getFirst(), spd3.getSecond());
					if (hfc <= b3) {
						hfc = b3;
						hs = spd3;
						hfv = a.getSecond();
						df = a.getFirst();
					}
			}
				if(hs.getFirst().size() == 0) {
					return new DTLeaf<>(mostPopularLabelFrom(hs.getSecond()));
				}
				else if(hs.getSecond().size() == 0) {
					return new DTLeaf<>(mostPopularLabelFrom(hs.getFirst()));
				}
				else if(hs.getFirst().size() >= 1 && hs.getSecond().size() >= 1) {
					t = new DTInterior<>(df, hfv, train(hs.getFirst()), train(hs.getSecond()), getFeatureValue, successor);
					return t;
				}
				else  {
					DTLeaf<V,L,F,FV> tl1 = new DTLeaf<>(data.get(0).getSecond());
					DTLeaf<V,L,F,FV> tl2 = new DTLeaf<>(data.get(1).getSecond());
					t = new DTInterior<>(df, hfv, tl1, tl2, getFeatureValue, successor);
					return t;
				}
		}
	}

	public static <V,L> L mostPopularLabelFrom(ArrayList<Duple<V, L>> data) {
		Histogram<L> h = new Histogram<>();
		for (Duple<V,L> datum: data) {
			h.bump(datum.getSecond());
		}
		return h.getPluralityWinner();
	}


	public static <V,L> ArrayList<Duple<V,L>> resample(ArrayList<Duple<V,L>> data) {
		Collections.shuffle(data);
		return data;
	}

	public static <V,L> double getGini(ArrayList<Duple<V,L>> data) {
		Histogram<L> h = new Histogram<>();
		for(Duple<V,L> a: data) {
			h.bump(a.getSecond());
		}
		double a = 0;
		for(L l: h) {
			a+= Math.pow((double)h.getCountFor(l)/h.getTotalCounts(), 2);
		}
		return 1.0 - a;
	}

	public static <V,L> double gain(ArrayList<Duple<V,L>> parent, ArrayList<Duple<V,L>> child1, ArrayList<Duple<V,L>> child2) {
		return getGini(parent) - (getGini(child1) + getGini(child2));
	}

	public static <V,L, F, FV  extends Comparable<FV>> Duple<ArrayList<Duple<V,L>>,ArrayList<Duple<V,L>>> splitOn(ArrayList<Duple<V,L>> data, F feature, FV featureValue, BiFunction<V,F,FV> getFeatureValue) {
		ArrayList<Duple<V,L>> c = new ArrayList<>();
		ArrayList<Duple<V,L>> d = new ArrayList<>();
		for(Duple<V,L> b: data) {
			if(featureValue.compareTo(getFeatureValue.apply(b.getFirst(), feature)) == 0) {
				c.add(b);
			}
			else {
				d.add(b);
			}
		}
		return new Duple<>(c,d);
	}
}
