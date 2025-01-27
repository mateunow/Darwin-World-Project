package darwinProject.model.maps;

import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.Animal;
import darwinProject.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class AbstractWorldMapTest {

    @Test
    void generateNewGrassPositions() {
    }

    @Test
    void handleMovement() {
    }

    @Test
    void handlePlantConsumption() {
    }

    @Test
    void handleReproduction() {
    }

    @Test
    void groupAnimalsByPositionAndFightForFood() {
        EarthMap map = new EarthMap(1,1,1,1,20);
        Animal animal1 = new Animal(new Vector2d(0,0), 7,50,5,1,0,3);
        Animal animal2 = new Animal(new Vector2d(0,0), 7,50,5,1,0,3);
        Animal animal3 = new Animal(new Vector2d(0,0), 7,50,5,1,0,3);
        try{
        map.place(animal1);
        map.place(animal3);
        map.place(animal2);}

        catch (IncorrectPositionException e) {
            e.getMessage();
        }
        assertEquals(150, animal1.getEnergy() + animal2.getEnergy() + animal3.getEnergy());
        map.groupAnimalsByPosition();
        map.handleMovement();
        map.handlePlantConsumption();
        assertEquals(110, animal1.getEnergy() + animal2.getEnergy() + animal3.getEnergy());

        Animal animal4 = new Animal(new Vector2d(0,0), 7,50,5,1,0,3);
        try{
            map.place(animal4);
        }
        catch (IncorrectPositionException e) {
            e.getMessage();
        }

        map.handleMovement();
        map.generateNewGrassPositions();
        map.handlePlantConsumption();

        assertEquals(30, animal4.getEnergy());
        assertEquals(50, animal1.getEnergy() + animal2.getEnergy() + animal3.getEnergy());
    }

    @Test
    void place() {
    }

    @Test
    void findFieldsWithoutGrass() {
    }
}