package robosim.ai;

import robosim.core.Controller;
import robosim.core.Simulator;
import robosim.reinforcement.QTable;
import robosim.core.Action;
public class Foward implements Controller {

    QTable qt = new QTable(3,2,1,100,3,0.25);
    int pf;

    @Override
    public void control(Simulator sim) {
        int s = sense(sim);
        int reward = 0;

        if(s == 1) {
            reward = 2;
        }
        if(s == 2) {
            reward = -10;
        }
        if(s == 3) {
            reward = -1;
        }
        int a = qt.senseActLearn(s-1,reward);
        if(a == 1) {
            Action.FORWARD.applyTo(sim);
        }
        else {
            Action.LEFT.applyTo(sim);
        }

    }

    private int sense(Simulator sim) {
        if(sim.getForwardMoves() > pf && sim.findClosestProblem() > 30) {
            pf = sim.getForwardMoves();
            return 1;
        }
        else if(sim.wasHit() && sim.getForwardMoves() > pf) {
            return 3;
        }
        else {
            return 2;
        }
    }
}
