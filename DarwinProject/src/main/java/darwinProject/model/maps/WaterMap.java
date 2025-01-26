package darwinProject.model.maps;

import darwinProject.enums.MapDirection;
import darwinProject.model.*;
import darwinProject.model.util.AnimalPriorityComparator;
import darwinProject.model.util.RandomPositionGenerator;

import java.util.*;

public class WaterMap extends AbstractWorldMap {
    private final Map<Vector2d, Water> waterMap = new HashMap<>();
    private final int numberOfWaterTiles;
    private final int width;
    private final int height;
    private Random random = new Random();


    public WaterMap(int height, int width, int startGrassCount, int numberOfPlantsGrownDaily, int energyFromEatingPlant) {
        super(height, width, startGrassCount, numberOfPlantsGrownDaily, energyFromEatingPlant);
        Random random = new Random();
        this.width = width;
        this.height = height;
        //TODO CHECK IF NOT -1
        this.numberOfWaterTiles = random.nextInt(height*width);
        generateWaterTiles();

    }

    // Generowanie losowych pól z wodą
    private void generateWaterTiles() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, numberOfWaterTiles);
        for (Vector2d waterPosition : randomPositionGenerator) {
            waterMap.put(waterPosition, new Water(waterPosition));
        }
    }
    @Override
    public void generateNewGrassPositions() {
        super.generateNewGrassPositions();
        if (random.nextBoolean()) {

        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !waterMap.containsKey(position) || position.precedes(upperRight) || position.follows(lowerLeft);
    }

    @Override
    public void move(Animal animal) {
        Vector2d currentPosition = animal.getPosition();
        MapDirection currentDirection = animal.getDirection();

        if (waterMap.containsKey(currentPosition)) {
            animal.die();  // Zwierzę umiera, gdy stanie na wodzie
            notifyObservers("Animal died due to water at " + currentPosition);
            return;
        }

        animal.move(this);
        Vector2d animalNewPosition = animal.getPosition();

        // Sprawdzenie, czy trawa jest na nowym polu i czy nie ma wody
        if (grassMap.containsKey(animalNewPosition) && !waterMap.containsKey(animalNewPosition)) {
            grassMap.remove(animalNewPosition);
        }

        // Sprawdzenie, czy trawa pojawiła się na polu z wodą
        if (waterMap.containsKey(animalNewPosition)) {
            grassMap.remove(animalNewPosition);  // Trawa umiera na wodzie
        }

        animals.remove(currentPosition);
        animals.computeIfAbsent(currentPosition, k -> new TreeSet<>(new AnimalPriorityComparator())).add(animal);


        if (!animal.getPosition().equals(currentPosition)) {
            notifyObservers("Animal moved from " + currentPosition + " to " + animal.getPosition());
        }
        if (!animal.getDirection().equals(currentDirection)) {
            notifyObservers("Animal turned from " + currentDirection + " to " + animal.getDirection());
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement waterElement = waterMap.get(position);
        if (waterElement != null) {
            return waterElement;
        }
        return super.objectAt(position);
    }

    public Set<Vector2d> findFieldsWithoutGrassAndWater() {
        // Usuwamy pola z wodą od wyników wyszukiwania pustych pól
        Set<Vector2d> fieldsWithoutGrass = super.findFieldsWithoutGrass();
        waterMap.keySet().forEach(fieldsWithoutGrass::remove);  // Usuwamy pola zajęte przez wodę
        return fieldsWithoutGrass;
    }

    public Map<Vector2d, Water> getWaterMap() {
        return waterMap;
    }


    public void spreadWater(Vector2d waterPosition) {
        // Sprawdzanie kierunków: góra, dół, lewo, prawo

//        TODO czy nie jest tak że da się to ładniej XD
        Vector2d[] directions = {
                new Vector2d(0, 1),   // Góra
                new Vector2d(0, -1),  // Dół
                new Vector2d(-1, 0),  // Lewo
                new Vector2d(1, 0)    // Prawo
        };

        for (Vector2d direction : directions) {
            Vector2d newPosition = waterPosition.add(direction);

                // Sprawdzamy, czy pole już nie ma wody
                if (waterMap.containsKey(newPosition)) {
                    continue;  // Jeśli już jest woda, przechodzimy do następnego kierunku
                }

                // Jeśli jest zwierzę na tym polu, zabijamy je
                if (animals.containsKey(newPosition)) {
                    animals.remove(newPosition); // Usuwamy zwierze
                    // TODO co z tym kill?
                    SortedSet<Animal> animalsToDelete = animals.get(newPosition);  // Ustalamy datę śmierci
                    for (Animal animal : animalsToDelete) {
                        animal.die();
                        animals.remove(animal);
                        deadAnimals.add(animal);
                    }
                    notifyObservers("Animal died due to water at " + newPosition);
                }

                // Jeśli jest trawa, usuwamy ją
                if (grassMap.containsKey(newPosition)) {
                    grassMap.remove(newPosition);  // Trawa umiera
                    notifyObservers("Grass died due to water at " + newPosition);
                }

                // Dodajemy wodę w tym miejscu
                waterMap.put(newPosition, new Water(newPosition));
                notifyObservers("Water spread to " + newPosition);

        }
    }

    public void drySomeWater(int divisor) {
        // Tworzymy listę z aktualnymi pozycjami wody
        List<Vector2d> waterPositions = new ArrayList<>(waterMap.keySet());

        // Losowo mieszamy pozycje
        Random random = new Random();
        Collections.shuffle(waterPositions, random); // Używamy Collections.shuffle()

        // Określamy liczbę pól, które mają wyschnąć na podstawie dzielnika
        int targetSize = waterPositions.size() / divisor;

        // Usuwamy określoną część wody
        for (int i = 0; i < targetSize; i++) {
            Vector2d waterPosition = waterPositions.get(i);

            // Usuwamy wodę z mapy
            waterMap.remove(waterPosition);

            // Powiadamiamy obserwatorów o wyschnięciu wody
            notifyObservers("Water at " + waterPosition + " has dried up.");
        }
    }
}
