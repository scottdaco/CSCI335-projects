package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.Iterator;
import java.util.function.ToIntFunction;


public class h2 implements ToIntFunction<MazeExplorer> {

    @Override
    public int applyAsInt(MazeExplorer node) {
        Iterator<Pos> a = node.getM().getTreasures().iterator();
        int current = 0;
        for(int i = 0; i < node.getM().getTreasures().size(); i++) {
            current = node.getLocation().getManhattanDist(a.next());
        }
        return current;
    }
}
