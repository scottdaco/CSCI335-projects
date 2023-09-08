package robosim.ai;

import core.Duple;
import robosim.core.*;
import robosim.reinforcement.QTable;

public class Dirt implements Controller {

    QTable qt = new QTable(5, 2, 2, 100, 1, 0.25);
    int pf;

    @Override
    public void control(Simulator sim) {
        int s = sense(sim);
        int reward = r(s);

        int a = qt.senseActLearn(s - 1, reward);
        if (a == 1) {
            Action.FORWARD.applyTo(sim);
        }
        else if(a == 2){
            Action.LEFT.applyTo(sim);
        }
    }


    private int r(int s) {
        if (s == 1) {
            return 2;
        }
        else if (s == 2) {
            return -10;
        }
        else if (s == 3) {
            return -1;
        }
        else if (s == 4) {
            return 5;
        }
        else if (s == 5) {
            return -1;
        }
        return 0;
    }

    private int sense(Simulator sim) {
        if (sim.getForwardMoves() > pf && sim.findClosestProblem() > 10) {
            pf = sim.getForwardMoves();
            return 1;
        } else if (sim.wasHit() && sim.getForwardMoves() > pf) {
            return 3;
        } else {
            for (Duple<SimObject, Polar> obj : sim.allVisibleObjects()) {
                if (obj.getFirst().isVacuumable()) {
                    return 4;
                }
                if (!obj.getFirst().isVacuumable()) {
                    return 5;
                }
            }
        }
        return 2;
    }
}