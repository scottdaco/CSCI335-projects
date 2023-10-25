package learning.som;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;

public class SelfOrgMap<V> {
    private V[][] map;
    private ToDoubleBiFunction<V, V> distance;
    private WeightedAverager<V> averager;

    public SelfOrgMap(int side, Supplier<V> makeDefault, ToDoubleBiFunction<V, V> distance, WeightedAverager<V> averager) {
        this.distance = distance;
        this.averager = averager;
        map = (V[][])new Object[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                map[i][j] = makeDefault.get();
            }
        }
    }

    public SOMPoint bestFor(V example) {
        double s = Integer.MAX_VALUE;
        SOMPoint a = null;
        for(int i = 0; i < getMapWidth(); i++) {
            for(int j = 0; j < getMapHeight(); j++) {
                double d = distance.applyAsDouble(getNode(i,j), example);
                if(d < s) {
                    s = d;
                    a = new SOMPoint(j,i);
                }
            }
        }
        return a;
    }

    public void train(V example) {
        SOMPoint bn = bestFor(example);
        V n = getNode(bn.x(), bn.y());
        map[bn.x()][bn.y()] = averager.weightedAverage(example, n, 0.9);
        Iterator<SOMPoint> itr = Arrays.stream(bn.neighbors()).iterator();
        for(int i = 0; i < bn.neighbors().length; i++) {
            SOMPoint nn = itr.next();
            if (nn.x() < getMapWidth() && nn.y() < getMapHeight() && nn.x() >= 0 && nn.y() >= 0) {
                V nnn = getNode(nn.x(), nn.y());
                map[nn.x()][nn.y()] = averager.weightedAverage(example, nnn, 0.4);
            }
        }
    }

    public V getNode(int x, int y) {
        return map[x][y];
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }

    public int numMapNodes() {
        return getMapHeight() * getMapWidth();
    }

    public boolean inMap(SOMPoint point) {
        return point.x() >= 0 && point.x() < getMapWidth() && point.y() >= 0 && point.y() < getMapHeight();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SelfOrgMap that) {
            if (this.getMapHeight() == that.getMapHeight() && this.getMapWidth() == that.getMapWidth()) {
                for (int x = 0; x < getMapWidth(); x++) {
                    for (int y = 0; y < getMapHeight(); y++) {
                        if (!map[x][y].equals(that.map[x][y])) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                result.append(String.format("(%d, %d):\n", x, y));
                result.append(getNode(x, y));
            }
        }
        return result.toString();
    }
}
