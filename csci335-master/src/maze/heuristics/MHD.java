package maze.heuristics;

import maze.core.MazeExplorer;
import java.util.function.ToIntFunction;


public class MHD implements ToIntFunction<MazeExplorer> {

    @Override
    public int applyAsInt(MazeExplorer node) {
        return node.getLocation().getManhattanDist(node.getM().getEnd());
    }
}
