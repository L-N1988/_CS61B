package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;
import huglife.HugLifeUtils;

import java.awt.Color;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;
    /** energy loss after moving. */
    private final double movLoss = 0.03;
    /** energy gain after staying. */
    private final double stayLoss = 0.01;
    /** fraction of energy to retain when replicating. */
    private final double repEnergyRetained = 0.5;
    /** fraction of energy to bestow upon offspring. */
    private final double repEnergyGiven = 0.5;

    /** creates Clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /** creates a Clorus with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    public Color color() {
        return color(r, g, b);
    }

    /** clorus gain energy after attacking */
    public void attack(Creature c) {
        energy += c.energy();
    }

    public void move() {
        energy = energy - movLoss;
    }


    public void stay() {
        energy = energy - stayLoss;
    }

    public Clorus replicate() {
        energy = energy * repEnergyRetained;
        double babyEnergy = energy * repEnergyGiven;
        return new Clorus(babyEnergy);
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        java.util.List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            Direction attDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, attDir);
        } else if (energy >= 1) {
            Direction repDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, repDir);
        } else {
            Direction movDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, movDir);
        }
    }

}
