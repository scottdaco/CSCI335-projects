package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Iterator;
import java.util.function.ToIntFunction;


public class NMh implements ToIntFunction<MazeExplorer> {

    @Override
    public int applyAsInt(MazeExplorer node) {
        Iterator<Pos> a = node.getM().getTreasures().iterator();
        int tcount = 0;
        int ecount = 0;
        for(int i = 0; i < node.getM().getTreasures().size(); i++) {
            tcount += node.getLocation().getManhattanDist(a.next());
        }
        a = node.getM().getTreasures().iterator();
        for(int i = 0; i < node.getM().getTreasures().size(); i++) {
            ecount += node.getM().getEnd().getManhattanDist(a.next());
        }
        return ecount + tcount + node.getLocation().getManhattanDist(node.getM().getEnd());
    }
}
