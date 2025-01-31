package darwinProject.model;

import darwinProject.enums.MapDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    @Test
    public void chceckTurn() {
        assertEquals(MapDirection.SOUTHEAST, MapDirection.NORTH.turn(3));
        assertEquals(MapDirection.SOUTHWEST, MapDirection.EAST.turn(3));
        assertEquals(MapDirection.SOUTHWEST, MapDirection.WEST.turn(7));
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.turn(0));
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTH.turn(8));
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTH.turn(9));
        assertEquals(MapDirection.SOUTHEAST, MapDirection.EAST.turn(1));
        assertEquals(MapDirection.NORTH, MapDirection.WEST.turn(2));
    }

    @Test
    public void chceckAngle() {
        assertEquals(0, MapDirection.NORTH.getAngle());
        assertEquals(90, MapDirection.EAST.getAngle());
        assertEquals(225, MapDirection.SOUTHWEST.getAngle());
        assertEquals(180, MapDirection.SOUTH.getAngle());
        assertEquals(315, MapDirection.NORTHWEST.getAngle());
    }
}