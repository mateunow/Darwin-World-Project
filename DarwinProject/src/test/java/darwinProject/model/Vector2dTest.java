package darwinProject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    void vector2dEqualsOther(){
        //when
        Vector2d vector1 = new Vector2d(1,2);
        Vector2d vector2 = new Vector2d(2,3);
        Vector2d vector3 = new Vector2d(1,2);

        //then
        assertFalse(vector1.equals(vector2));
        assertTrue(vector1.equals(vector3));
    }
    @Test
    void vector2dtoString(){
        //when
        Vector2d vector1 = new Vector2d(1,2);

        //then
        assertEquals("(1, 2)",vector1.toString());

    }
    @Test
    void vector2dPrecedesOther(){
        //when
        Vector2d vector1 = new Vector2d(1,2);
        Vector2d vector2 = new Vector2d(0,3);
        Vector2d vector3 = new Vector2d(1,6);

        //then
        assertFalse(vector1.precedes(vector2));
        assertTrue(vector1.precedes(vector3));
        //tu powinno być 9 testów dla każdego
    }
    @Test
    void vector2dFollowsOther(){
        //when
        Vector2d vector1 = new Vector2d(1,2);
        Vector2d vector2 = new Vector2d(0,-10);
        Vector2d vector3 = new Vector2d(-1,6);

        //then
        assertFalse(vector1.follows(vector3));
        assertTrue(vector1.follows(vector2));
    }
    @Test
    void upperRightVector2dAndOther(){
        //when
        Vector2d vector1 = new Vector2d(1,2);
        Vector2d vector2 = new Vector2d(0,3);

        //then
        assertEquals(new Vector2d(1,3), vector1.upperRight(vector2));
        assertEquals(new Vector2d(1,3), vector2.upperRight(vector1));
        assertEquals(new Vector2d(0,3), vector2.upperRight(vector2));

    }
    @Test
    void lowerLeftVector2dAndOther() {
        //when
        Vector2d vector1 = new Vector2d(1,-13);
        Vector2d vector2 = new Vector2d(-15,3);

        //then
        assertEquals(new Vector2d(-15,-13), vector1.lowerLeft(vector2));
        assertEquals(new Vector2d(-15,3), vector2.lowerLeft(vector2));
    }
    @Test
    void vector2dAddOther(){
        //when
        Vector2d vector1 = new Vector2d(1,2);
        Vector2d vector2 = new Vector2d(-1,7);

        //then
        assertEquals(new Vector2d (0,9), vector1.add(vector2));
        assertEquals(new Vector2d (2,4), vector1.add(vector1));
    }

    @Test
    void vector2dSubtractOther(){
        //when
        Vector2d vector1 = new Vector2d(1,2);
        Vector2d vector2 = new Vector2d(-1,7);

        //then
        assertEquals(new Vector2d (2,-5), vector1.subtract(vector2));
        assertEquals(new Vector2d (0,0), vector1.subtract(vector1));
    }

    @Test
    void vector2dOpposite(){
        //when
        Vector2d vector1 = new Vector2d(-1,2);

        //then
        assertEquals(new Vector2d(1,-2), vector1.opposite());
    }

}