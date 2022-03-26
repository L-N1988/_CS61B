package creatures;
import huglife.Action;
import huglife.Impassible;
import huglife.Direction;
import huglife.Occupant;
import huglife.Empty;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;

public class TestClorus {
    @Test
    public void testChoose() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Empty());

        surrounded.put(Direction.RIGHT, new Plip(1));
        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.ATTACK, Direction.RIGHT);
        assertEquals(expected, actual);

        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        surrounded.put(Direction.RIGHT, new Empty());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);
        assertEquals(expected, actual);
    }
}
