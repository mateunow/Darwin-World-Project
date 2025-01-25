package darwinProject.model;

import darwinProject.enums.MapDirection;
import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.maps.EarthMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    public void startingAnimal() {
        Integer genomeSize = 20;
        Vector2d position = new Vector2d(0, 0);
        Animal animal = new Animal(position, genomeSize, 50, 30, 20, 0, 3);

        System.out.println(animal.getGenome());
        assertEquals(genomeSize, animal.getGenome().size());
    }

    @Test
    public void testTurn() {
        Animal animal = new Animal(new Vector2d(2,2), 7, 50, 30, 20, 0, 3);
        MapDirection direction = animal.getDirection();
        System.out.println(animal + " " + direction);
        animal.turn(4);
        System.out.println(animal + " " + direction);
        assertEquals(direction.turn(4), animal.getDirection());
        direction = direction.turn(4);
        animal.turn(3);
        System.out.println(animal + " " + direction);
        assertEquals(direction.turn(3), animal.getDirection());


    }

    @Test
    public void testReproduction() {
        Animal animal = new Animal(new Vector2d(2,2), 7, 70, 30, 20, 0, 3);
        Animal animal2 = new Animal(new Vector2d(2,2), 7, 50, 30, 20, 0, 3);
        System.out.println("First animal genome: " + animal.getGenome());
        System.out.println("Second animal genome: " + animal2.getGenome());

        Animal animal3 = animal.reproduceWithOtherAnimal(animal2);
        assertEquals(50, animal.getEnergy());
        assertEquals(30, animal2.getEnergy());
        assertEquals(40, animal3.getEnergy());
        assertEquals(0, animal3.getChildrenCount());
        assertEquals(1, animal2.getChildrenCount());
        assertEquals(1, animal.getChildrenCount());
        System.out.println("Child genome: " + animal3.getGenome());
    }

    @Test
    public void testEnergyAfterReproduction() {
        Animal animal = new Animal(new Vector2d(2,2), 7, 70, 30, 20, 0, 3);
        Animal animal2 = new Animal(new Vector2d(2,2), 7, 50, 30, 20, 0, 3);
        System.out.println("First animal genome: " + animal.getGenome());
        System.out.println("Second animal genome: " + animal2.getGenome());
        Animal animal3 = animal.reproduceWithOtherAnimal(animal2);
    }
    @Test
    public void eatGrassTestSingleAnimal() {
        Animal animal = new Animal(new Vector2d(2,2), 7, 50, 30, 20, 0, 3);
        EarthMap map = new EarthMap(10,10,100,1,20);
        try {
            map.place(animal);
        }
        catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(map);

        for (int i = 0; i < 10; i++) {
            map.move(animal);
            map.generateNewGrassPositions();
            System.out.println(map);
            System.out.println(animal.getEnergy());
            assertEquals(50 + 20*(i+1),animal.getEnergy());
        }
        assertEquals(250, animal.getEnergy());

    }

    @Test
    public void eatGrassTestMultipleAnimals() {
        Animal animal1 = new Animal(new Vector2d(1,1), 7, 50, 30, 20, 0, 3);
        Animal animal2 = new Animal(new Vector2d(1,1), 7, 50, 30, 20, 0, 3);
        Animal animal3 = new Animal(new Vector2d(1,1), 7, 50, 30, 20, 0, 3);
        Animal animal4 = new Animal(new Vector2d(1,1), 7, 50, 30, 20, 0, 3);
        EarthMap map = new EarthMap(2,2,4,1,20);
        System.out.println(map);
        try {
            map.place(animal1);
            map.place(animal2);
            map.place(animal3);
            map.place(animal4);
        }
        catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(map);
        for (int i = 0; i < 10; i++) {
            map.move(animal1);
            map.move(animal2);
            map.move(animal3);
            map.move(animal4);
            System.out.println(map);
        }
    }

    @Test
    void move() {
    }

}