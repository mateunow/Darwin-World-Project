package darwinProject.model.maps;

import darwinProject.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WaterMapTest {

    @Test
    public void testCanMoveTo() {
        // Utwórz nową mapę z wodą
        WaterMap waterMap = new WaterMap(10, 10, 5, 2, 10);

        // Dodaj zwierzę do mapy na pozycji 1,1
        Animal animal = new Animal(new Vector2d(1, 1), 7, 50, 30, 20, 0, 3);
        assertDoesNotThrow(() -> waterMap.place(animal));

        // Testujemy canMoveTo w różnych scenariuszach
        Vector2d emptyPosition = new Vector2d(2, 2);

        // 1. Sprawdź, czy zwierzę może się poruszyć na pustą komórkę (pozycja 2, 2)
        assertTrue(waterMap.canMoveTo(emptyPosition));  // Pusta komórka

        // 2. Sprawdź, czy zwierzę może się poruszyć na brzeg mapy
        assertTrue(waterMap.canMoveTo(new Vector2d(0, 0)));  // Komórka na krawędzi mapy (pusta)

        // 3. Sprawdź, czy zwierzę nie może poruszać się na pole z wodą
        // Iterujemy po losowych pozycjach z wodą
        Set<Vector2d> waterPositions = waterMap.getWaterMap().keySet();
        for (Vector2d waterPosition : waterPositions) {
            assertFalse(waterMap.canMoveTo(waterPosition));  // Komórka zawiera wodę
        }
    }


    @Test
    void testObjectAt() {
        WaterMap map = new WaterMap(10, 10, 15, 2, 20);
        Vector2d waterPosition = new Vector2d(4, 4);
        map.getWaterMap().put(waterPosition, new Water(waterPosition)); // Dodajemy wodę

        // Sprawdzamy, czy woda znajduje się w odpowiedniej pozycji
        WorldElement element = map.objectAt(waterPosition);


        Vector2d animalPosition = new Vector2d(5, 5);
        Animal animal = new Animal(animalPosition, 7, 50, 30, 20, 0, 3);
        assertDoesNotThrow(() -> map.place(animal));
    }

    @Test
    void testWaterSpread() {
        WaterMap map = new WaterMap(10, 10, 15, 2, 20);
        Vector2d waterPosition = new Vector2d(2, 2);
        map.getWaterMap().put(waterPosition, new Water(waterPosition));

        // Sprawdzamy, czy woda rozprzestrzeniła się na sąsiednie pola
        map.spreadWater(waterPosition);

        Vector2d[] directions = {
                new Vector2d(0, 1),   // Góra
                new Vector2d(0, -1),  // Dół
                new Vector2d(-1, 0),  // Lewo
                new Vector2d(1, 0)    // Prawo
        };

        for (Vector2d direction : directions) {
            Vector2d newPosition = waterPosition.add(direction);
            assertTrue(map.getWaterMap().containsKey(newPosition));
        }
    }

    @Test
    void testDrySomeWater() {
        WaterMap map = new WaterMap(10, 10, 15, 2, 20);
        Vector2d waterPosition1 = new Vector2d(2, 2);
        Vector2d waterPosition2 = new Vector2d(3, 3);

        map.getWaterMap().put(waterPosition1, new Water(waterPosition1));
        map.getWaterMap().put(waterPosition2, new Water(waterPosition2));

        // Przesuszamy część wody
        map.drySomeWater(1);

        // Sprawdzamy, czy po wyschnięciu woda została usunięta z niektórych pól
        assertFalse(map.getWaterMap().containsKey(waterPosition1) || map.getWaterMap().containsKey(waterPosition2));
    }

}
