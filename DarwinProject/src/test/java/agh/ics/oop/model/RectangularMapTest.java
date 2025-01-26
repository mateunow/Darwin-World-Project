package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    @Test
    public void testStarMap() {
        //given
        WorldMap map = new RectangularMap(3, 3);
        Animal animal = new Animal();

        //when
        animal.setPosition(new Vector2d(1, 1));
        assertDoesNotThrow(() -> {map.place(animal);});

        //then
        assertFalse(map.canMoveTo(new Vector2d(3, 2)));
        assertFalse(map.canMoveTo(new Vector2d(5, 5)));
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertTrue(map.canMoveTo(new Vector2d(2, 2)));
        assertTrue(map.canMoveTo(new Vector2d(2, 2)));
        assertFalse(map.isOccupied(new Vector2d(2, 1)));
        assertTrue(map.isOccupied(new Vector2d(1, 1)));
        assertFalse(map.isOccupied(new Vector2d(2, 2)));
    }

    @Test
    public void testPutDifferentAnimalsOnCorrectAndIncorrectPositions() {
        //given
        WorldMap map = new RectangularMap(3, 3);
        Animal animal0 = new Animal();
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();
        Animal animal3 = new Animal();
        Animal animal4 = new Animal();

        //when
        animal2.setPosition(new Vector2d(0, 1));
        animal3.setPosition(new Vector2d(3, 3));
        animal4.setPosition(new Vector2d(0, 4));

        //then
        assertDoesNotThrow(() -> {map.place(animal0);});


        assertFalse(map.canMoveTo(animal0.getPosition()));
        assertEquals(animal0, map.objectAt(new Vector2d(2, 2)));
        assertThrows(IncorrectPositionException.class, () -> map.place(animal1));
        assertNull(map.objectAt(new Vector2d(0, 3)));
        assertDoesNotThrow(() -> {map.place(animal2);});


        assertThrows(IncorrectPositionException.class, () -> map.place(animal3));
        assertThrows(IncorrectPositionException.class, () -> map.place(animal4));

    }

    @Test
    public void testMapWithTwoAnimalsOneTryingToCrossOther() {
        //given
        WorldMap map = new RectangularMap(5, 5);
        Animal animal0 = new Animal();
        Animal animal1 = new Animal();

        //when
        animal0.setPosition(new Vector2d(1, 1));
        animal1.setPosition(new Vector2d(1, 2));
        assertDoesNotThrow(() -> {map.place(animal0);});

        assertDoesNotThrow(() -> {map.place(animal1);});


        //uznałem, że te 2 println-y pomogą zrozumieć innemu użytkownikowi na czym polega ten test
        System.out.println(map);
        map.move(animal0, MoveDirection.FORWARD);
        System.out.println(map);

        //then
        assertTrue(map.isOccupied(new Vector2d(1, 1)));
        assertTrue(map.isOccupied(new Vector2d(1, 2)));

    }

    @Test
    public void testAnimalCannotMoveOutOfBounds() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();

        //when
        assertDoesNotThrow(() -> {map.place(animal);});


        System.out.println(map);
        map.move(animal, MoveDirection.FORWARD);
        System.out.println(map);
        map.move(animal, MoveDirection.FORWARD);
        System.out.println(map);
        map.move(animal, MoveDirection.FORWARD);
        System.out.println(map);
        map.move(animal, MoveDirection.FORWARD);
        System.out.println(map);

        //then
        assertEquals(new Vector2d(2, 4), animal.getPosition());


        //when
        map.move(animal, MoveDirection.RIGHT);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        //then
        assertEquals(new Vector2d(4, 4), animal.getPosition());

        //when
        map.move(animal, MoveDirection.RIGHT);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        //then
        assertEquals(new Vector2d(4, 0), animal.getPosition());

        //when
        map.move(animal, MoveDirection.BACKWARD);
        map.move(animal, MoveDirection.RIGHT);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        map.move(animal, MoveDirection.FORWARD);
        //then
        assertEquals(new Vector2d(0, 1), animal.getPosition());

    }

    @Test
    public void threeAnimalsTryingToReachOneCell() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        map.registerObservers(new ConsoleMapDisplay());
        Animal animal0 = new Animal();
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();

        //when
        animal0.setPosition(new Vector2d(1, 1));
        animal1.setPosition(new Vector2d(2, 2));
        animal2.setPosition(new Vector2d(1, 3));
        assertDoesNotThrow(() -> {map.place(animal0);});
        assertDoesNotThrow(() -> {map.place(animal1);});
        assertDoesNotThrow(() -> {map.place(animal2);});


        map.move(animal0, MoveDirection.FORWARD);
        map.move(animal1, MoveDirection.LEFT);
        map.move(animal2, MoveDirection.BACKWARD);
        map.move(animal0, MoveDirection.FORWARD);
        map.move(animal1, MoveDirection.FORWARD);
        map.move(animal2, MoveDirection.BACKWARD);

        //then
        assertEquals(new Vector2d(1,2),animal0.getPosition());
        assertEquals(new Vector2d(2,2),animal1.getPosition());
        assertEquals(new Vector2d(1,3),animal2.getPosition());
        assertTrue(map.isOccupied(new Vector2d(1, 2)));
        assertFalse(map.canMoveTo(new Vector2d(1, 2)));
    }
}