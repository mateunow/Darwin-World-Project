package darwinProject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class GrassTest {

    @Test
    public void grassPositionTest() {
        Grass grass0 = new Grass(new Vector2d(2,2));
        Grass grass1 = new Grass(new Vector2d(3,3));
        Grass grass2 = new Grass(new Vector2d(2,2));
        Grass grass3 = new Grass(new Vector2d(2,3));

        assertEquals(new Vector2d(2,2), grass0.getPosition());
        assertEquals(new Vector2d(3,3), grass1.getPosition());
        assertEquals(new Vector2d(2,2), grass2.getPosition());
        assertEquals(new Vector2d(2,3), grass3.getPosition());
    }

    @Test
    public void toStringTest(){
        Grass grass0 = new Grass(new Vector2d(2,2));

        assertEquals("*", grass0.toString());
    }
}