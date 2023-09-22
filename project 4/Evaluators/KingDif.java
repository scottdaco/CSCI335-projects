package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class KingDif implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        if(value.numKingsOf(value.getCurrentPlayer()) <= value.numKingsOf(value.getCurrentPlayer().opponent())) {
            return 1;
        }
        else {
            return -1;
        }

    }
}
