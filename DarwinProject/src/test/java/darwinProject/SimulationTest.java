package darwinProject;

import darwinProject.model.*;
import darwinProject.enums.MapDirection;
import darwinProject.model.maps.EarthMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {


//    @Test
//    public void testRunWithValidMoveDirection () {
//        //given
//        EarthMap map = new EarthMap(5,5,1);
//        List<Vector2d> positions =List.of(new Vector2d(2,2));
//
//
//        //when
//        Simulation simulation = new Simulation(positions, map, 8 , 50);
//        simulation.run();
//        List<Animal> animals = simulation.getAnimals();
//        Animal simulatedAnimal = animals.getFirst();
//
//        //then
//        assertEquals(1, animals.size());
//        assertTrue(simulatedAnimal.getPosition().precedes(new Vector2d(4, 4)) && simulatedAnimal.getPosition().follows(new Vector2d(0, 0)));
//        assertEquals(MapDirection.EAST, simulatedAnimal.getDirection());
//
//
//    }

//    @Test
//    public void testRunWithException () {
//        //given
//        WorldMap map = new RectangularMap(5, 5);
//        String[] directionsArray = "m a v x c h m p".split(" ");
//
//        try {
//            List<MoveDirection> directions = OptionsParser.parse(directionsArray);
//        } catch (IllegalArgumentException e) {
//            assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(directionsArray));
//        }
        //TODO replace with new run method
//    }


//    @Test
//    public void testSimulationWithTwoAnimalsCorrectDirectionsAndCrossingEachOther () {
//        //given
//        EarthMap map = new EarthMap(5,5,1);
//         List<Vector2d> positions =List.of(new Vector2d(0,4), new Vector2d(2,2));
//
//        //when
//        Simulation simulation = new Simulation(positions, map, 7, 50);
//        simulation.run();
//        List<Animal> animals = simulation.getAnimals();
//        Animal simulatedAnimal0 = animals.get(0);
//        Animal simulatedAnimal1 = animals.get(1);
//
//        //then
//        assertEquals(2, animals.size());
//        assertTrue(simulatedAnimal0.getPosition().precedes(new Vector2d(4, 4)) && simulatedAnimal0.getPosition().follows(new Vector2d(0, 0)));
//        assertTrue(simulatedAnimal1.getPosition().precedes(new Vector2d(4,4)) && simulatedAnimal1.getPosition().follows(new Vector2d (0,0)));
//        assertEquals(MapDirection.SOUTH, simulatedAnimal0.getDirection());
//        assertEquals(MapDirection.WEST, simulatedAnimal1.getDirection());
//        assertTrue(simulatedAnimal0.isAt(new Vector2d (0,1)));
//        assertTrue(simulatedAnimal1.isAt(new Vector2d (0, 4)));
//
//
//    }

//    @Test
//    public void testStartAnimalsOneCorrectOneWithWrongStartingPositionSet () {
//        EarthMap map = new EarthMap(5,5,1);
//        List<Vector2d> positions =List.of(new Vector2d(2,2), new Vector2d(-1,30));
//
//        Simulation simulation = new Simulation(positions, map, 7, 50);
//        simulation.run();
//
//
//    }
}