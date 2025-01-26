package darwinProject.model.maps;

import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.Animal;
import darwinProject.model.Grass;
import darwinProject.model.Vector2d;
import darwinProject.model.WorldElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EarthMapTest {

    @Test
    public void testStartingMap() {
        EarthMap map = new EarthMap(10, 10, 15,2,20);
        Animal animal = new Animal(new Vector2d(1,1), 7, 50, 30, 20, 0, 3);
        Animal animal2 = new Animal(new Vector2d(3,3), 7, 50, 30, 20, 0, 3);
        Animal animal3 = new Animal(new Vector2d(8,9), 7, 50, 30, 20, 0, 3);
        Animal animal4 = new Animal(new Vector2d(9,8), 7, 50, 30, 20, 0, 3);
        Animal animal5 = new Animal(new Vector2d(9,9), 7, 50, 30, 20, 0, 3);

        //when
        assertDoesNotThrow(() -> map.place(animal));
        assertDoesNotThrow(() -> map.place(animal2));
        assertDoesNotThrow(() -> map.place(animal3));
        assertDoesNotThrow(() -> map.place(animal4));
        assertDoesNotThrow(() -> map.place(animal5));
//        assertThrows(IncorrectPositionException.class, () -> {map.place(animal2);});
        System.out.println(map);

        for (int i= 0; i < 100; i++) {
            map.move(animal);
            map.move(animal2);
            map.move(animal3);
            map.move(animal4);
            map.move(animal5);
            System.out.println(map);
        }
    }
    @Test
    void canMoveTo() {
    }

    @Test
    public void testObjectAt() {
        //given
        EarthMap earthMap = new EarthMap(16, 16, 10,2,20);
        Vector2d grassPosition = earthMap.getElements().stream()
                .filter(element -> element instanceof Grass)
                .map(WorldElement::getPosition)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No grass found"));

        //when
        Vector2d animalPosition = new Vector2d(3, 3);
        Animal animal = new Animal(animalPosition, 7, 50, 30, 20, 0, 3);
        assertDoesNotThrow(() -> {
            earthMap.place(animal);
        });

        //then
        boolean somethingAtPosition = ((earthMap.objectAt(grassPosition) instanceof Animal) || (earthMap.objectAt(grassPosition) instanceof Grass));
        assertTrue(somethingAtPosition);
        assertEquals(animal, earthMap.objectAt(animalPosition));
    }

    @Test
    public void testPlaceAnimalOnCorrectAndIncorrectPosition() {
        //given
        EarthMap grassField = new EarthMap(40, 40, 10,2,20);
        Vector2d position = new Vector2d(1, 1);
        Animal animal0 = new Animal(position, 7, 50, 30, 20, 0, 3);

        //then
        assertDoesNotThrow(() -> {grassField.place(animal0);});


        assertEquals(animal0, grassField.objectAt(position));

        Animal animal1 = new Animal(new Vector2d(30,11), 7, 50, 30, 20, 0, 3);
        Animal animal2 = new Animal(new Vector2d(50,51), 7, 50, 30, 20, 0, 3);
        //then
        assertDoesNotThrow(() -> {grassField.place(animal1);});
        assertThrows(IncorrectPositionException.class, () -> grassField.place(animal2));
        assertEquals(animal1, grassField.objectAt(new Vector2d(30,11)));
    }

}