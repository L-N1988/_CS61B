package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;
import huglife.HugLifeUtils;

import java.awt.Color;
import java.util.Map;
import java.util.List;

/** An implementation of a motile pacifist photosynthesizer.
 *  @author Josh Hug
 */
public class Plip extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;
    /** energy loss after moving. */
    private final double movLoss = 0.15;
    /** energy gain after staying. */
    private final double stayGain = 0.2;
    /** fraction of energy to retain when replicating. */
    private final double repEnergyRetained = 0.5;
    /** fraction of energy to bestow upon offspring. */
    private final double repEnergyGiven = 0.5;

    /** creates plip with energy equal to E. */
    public Plip(double e) {
        super("plip");
        energy = (e > 2) ? 2 : e;
        r = 99;
        g = (int) (63 + e / 2.0 * (255 - 63));
        b = 76;
    }

    /** creates a plip with energy equal to 1. */
    public Plip() {
        this(1);
    }

    /** Should return a color with red = 99, blue = 76, and green that varies
     *  linearly based on the energy of the Plip. If the plip has zero energy,
     *  it should have a green value of 63. If it has max energy, it should
     *  have a green value of 255. The green value should vary with energy
     *  linearly in between these two extremes. It's not absolutely vital
     *  that you get this exactly correct.
     */
    public Color color() {
        g = (int) (63 + energy / 2.0 * (255 - 63));
        return color(r, g, b);
    }

    /** Do nothing with C, Plips are pacifists. */
    public void attack(Creature c) {
    }

    /** Plips should lose 0.15 units of energy when moving. If you want
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        energy = energy - movLoss;
    }


    /** Plips gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
        energy = energy + stayGain;
        // max energy is 2
        energy = (energy > 2) ? 2 : energy;
    }

    /** Plips and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Plip.
     */
    public Plip replicate() {
        energy = energy * repEnergyRetained;
        double babyEnergy = energy * repEnergyGiven;
        return new Plip(babyEnergy);
    }

    /** Plips take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if energy >= 1, REPLICATE.
     *  3. Otherwise, if any Cloruses, MOVE with 50% probability.
     *  4. Otherwise, if nothing else, STAY
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> enemies = getNeighborsOfType(neighbors, "clorus");
        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (energy >= 1) {
            Direction movDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, movDir);
        } else if (!enemies.isEmpty()) {
            Direction movDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, movDir);
        } else {
            return new Action(Action.ActionType.STAY);
        }
    }

}
