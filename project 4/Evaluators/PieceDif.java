package checkers.evaluators;

import checkers.core.Checkerboard;

import java.awt.*;
import java.util.function.ToIntFunction;

public class PieceDif implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        if(value.numPiecesOf(value.getCurrentPlayer()) > value.numPiecesOf(value.getCurrentPlayer().opponent())) {
            return value.numPiecesOf(value.getCurrentPlayer()) - value.numPiecesOf(value.getCurrentPlayer().opponent());
        }
        else {
            return value.numPiecesOf(value.getCurrentPlayer().opponent()) - value.numPiecesOf(value.getCurrentPlayer());
        }

    }
}
