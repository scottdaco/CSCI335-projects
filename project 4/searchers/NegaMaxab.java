package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMaxab extends CheckersSearcher {

    PlayerColor c = PlayerColor.RED;
    public NegaMaxab(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return 0;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return negamax(board, getDepthLimit(), -Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private Optional<Duple<Integer, Move>> negamax(Checkerboard board, int depth, int a, int b) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        int bestscore = -Integer.MAX_VALUE;
        Move bestmove = null;
        if(board.gameOver()) {
            best = Optional.of(new Duple<>(wl(board), bestmove));
            return best;
        }
        else if(depth <= 0) {
            best = Optional.of(new Duple<>(getEvaluator().applyAsInt(board), bestmove));
            return best;
        }
        for(Move m: board.getCurrentPlayerMoves()){
            Checkerboard nb = board.duplicate();
            nb.move(m);
            int value;
//            best = negamax(nb, depth-1, -b, -a);
            if(board.getCurrentPlayer().equals(c)) {
                best = negamax(nb, depth-1, a, b);
                value = best.get().getFirst();
            }
            else {
                best = negamax(nb, depth-1, -b, -a);
                c.equals(board.getCurrentPlayer());
                value = -best.get().getFirst();
            }
            if(value > bestscore) {
                bestscore = value;
                bestmove = m;
                a = bestscore;
            }
            if(a >= b) {
                break;
            }
        }
        best = Optional.of(new Duple<>(bestscore, bestmove));
        return best;
    }

    private int wl(Checkerboard board) {
        if (board.playerWins(board.getCurrentPlayer())) {
            return Integer.MAX_VALUE;
        }
        else if(board.playerWins(board.getCurrentPlayer())) {
            return -Integer.MAX_VALUE;
        }
        else {
            return 0;
        }
    }

}
