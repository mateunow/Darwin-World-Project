package darwinProject.model.maps;

import darwinProject.model.*;
import darwinProject.model.util.AnimalPriorityComparator;
import darwinProject.model.util.Boundary;
import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.util.MapVisualizer;
import darwinProject.model.util.RandomPositionGenerator;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected final Map<Vector2d, SortedSet<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Grass> grassMap = new HashMap<>();
    private Set<Vector2d> fieldsWithoutGrass = new HashSet<>();
    List<Animal> livingAnimals = new ArrayList<>();


    protected final Vector2d upperRight;
    protected final Vector2d lowerLeft = new Vector2d(0,0);
    private final Boundary finalBoundary;
    private final HashSet<MapChangeListener> observers = new HashSet<>();
    protected final int id = this.hashCode();
    protected final Set<Animal> deadAnimals = new HashSet<>();
    private final Integer numberOfPlantsGrownDaily;
    private final Integer energyFromEatingPlant;;

    protected AbstractWorldMap(int height, int width, int startGrassCount, int numberOfPlantsGrownDaily, int energyFromEatingPlant) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, startGrassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
        }
        this.upperRight = new Vector2d(width - 1,height - 1);
        this.finalBoundary = new Boundary(lowerLeft, upperRight);
        this.fieldsWithoutGrass = findFieldsWithoutGrass();
        this.numberOfPlantsGrownDaily = numberOfPlantsGrownDaily;
        this.energyFromEatingPlant = energyFromEatingPlant;
    }


    public final void registerObservers(MapChangeListener observer) {
        observers.add(observer);
    }
    public final void unregisterObservers(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    public void generateNewGrassPositions() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(new ArrayList<>(fieldsWithoutGrass), numberOfPlantsGrownDaily);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
            fieldsWithoutGrass.remove(grassPosition);
            notifyObservers("New grass added");
        }
    }
    public void handleMovement() {
        List<Animal> deadAnimalsToRemove = new ArrayList<>();
        System.out.println(livingAnimals.size());
        for (Animal animal : livingAnimals) {
            this.move(animal);

            if (animal.isDead()) {
                deadAnimals.add(animal);
                deadAnimalsToRemove.add(animal);
                Vector2d position = animal.getPosition();
                SortedSet<Animal> animalsAtPosition = animals.get(position);
                if (animalsAtPosition != null) {
                    animalsAtPosition.remove(animal);
                    if (animalsAtPosition.isEmpty()) {
                        animals.remove(position);
                    }
                }
            }
        }
        livingAnimals.removeAll(deadAnimalsToRemove);
        notifyObservers("Living animals moved, dead ones removed");
    }


    public void handlePlantConsumption() {
        Map<Vector2d, List<Animal>> groupedAnimals = groupAnimalsByPosition();

        if (!grassMap.isEmpty()) {
            List<Vector2d> positionsToRemove = new ArrayList<>();

            for (Map.Entry<Vector2d, Grass> entry : grassMap.entrySet()) {
                Vector2d position = entry.getKey();
                List<Animal> animalsAtPosition = groupedAnimals.get(position);

                if (animalsAtPosition != null && !animalsAtPosition.isEmpty()) {
                    Animal strongestAnimal = getStrongestAnimal(animalsAtPosition);
                    strongestAnimal.addEnergy(energyFromEatingPlant);
                    positionsToRemove.add(position);
                }
            }

            for (Vector2d position : positionsToRemove) {
                grassMap.remove(position);
            }
        }
        notifyObservers("Plants eaten");
    }


    public void handleReproduction() {
        Map<Vector2d, List<Animal>> groupedAnimals = groupAnimalsByPosition();

        for (Map.Entry<Vector2d, List<Animal>> entry : groupedAnimals.entrySet()) {
            List<Animal> animalsAtPosition = entry.getValue();

            if (animalsAtPosition.size() > 1) {
                animalsAtPosition.sort(new AnimalPriorityComparator());

                for (int i = 0; i < animalsAtPosition.size() - 1; i += 2) {
                    Animal parent1 = animalsAtPosition.get(i);
                    Animal parent2 = animalsAtPosition.get(i + 1);

                    if (parent1.canReproduce() && parent2.canReproduce()) {
                        Animal child = parent1.reproduceWithOtherAnimal(parent2);
                        try {
                            placeAnimal(child, entry.getKey());
                        }
                        catch (Exception e) {
                            System.out.println("Nie udało się postawić zwierzęcia" + e.getMessage());
                        }
                    }
                }
            }
        }
        notifyObservers("Reproduction");
    }
    public Grass grassAt(Vector2d position) {
        return grassMap.get(position);
    }

    protected Map<Vector2d, List<Animal>> groupAnimalsByPosition() {
        Map<Vector2d, List<Animal>> grouped = new HashMap<>();

        for (Map.Entry<Vector2d, SortedSet<Animal>> entry : animals.entrySet()) {
            Vector2d position = entry.getKey();
            SortedSet<Animal> animalSet = entry.getValue();

            // Add all animals at this position to the list
            grouped.computeIfAbsent(position, k -> new ArrayList<>()).addAll(animalSet);
        }
        return grouped;
    }

    public abstract void move(Animal animal) ;

    protected Animal getStrongestAnimal(List<Animal> animals) {
        if (animals == null || animals.isEmpty()) {
            throw new IllegalStateException("No animals at position");
        }
        return animals.stream().min(new AnimalPriorityComparator())
                .orElseThrow(() -> new IllegalStateException("No animals at position"));
    }

    protected void placeAnimal(Animal animal, Vector2d position) throws IncorrectPositionException {
        if (canMoveTo(position)) {
            animals.computeIfAbsent(position, k -> new TreeSet<>()).add(animal);
            livingAnimals.add(animal);
        }
        else {
            throw new IncorrectPositionException(position);
        }
    }


    @Override
    public abstract boolean canMoveTo(Vector2d position);

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public void place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();
        if (canMoveTo(position)) {
            animals.computeIfAbsent(position, k -> new TreeSet<>(new AnimalPriorityComparator())).add(animal);
            livingAnimals.add(animal);
            notifyObservers("Animal placed at " + animal.getPosition());
        } else {
            throw new IncorrectPositionException(position);
        }
    }



    public void eatPlants() {
        if (!livingAnimals.isEmpty()) {
            Map<Vector2d, List<Animal>> animalsGroupedByPosition = groupAnimalsByPosition();

            for (Map.Entry<Vector2d, List<Animal>> entry : animalsGroupedByPosition.entrySet()) {
                Vector2d position = entry.getKey();
                List<Animal> animals = entry.getValue();
                Grass grass = grassMap.get(position);

                if (grass != null && !animals.isEmpty()) {
                    animals.sort(new AnimalPriorityComparator());
                    Animal topAnimal = animals.get(0);  // Sorted in reverse order, so the first is the strongest
                    topAnimal.addEnergy(energyFromEatingPlant);
                    grassMap.remove(position);
                    fieldsWithoutGrass.add(position);
                }
            }
        }
    }

    public Set<Vector2d> findFieldsWithoutGrass() {
        Boundary boundary = getCurrentBounds();
        int width = boundary.upperRight().getY();
        int height = boundary.upperRight().getX();

        Set<Vector2d> fieldsWithoutGrassSet = new HashSet<>();
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                Vector2d field = new Vector2d(i, j);
                if (!grassMap.containsKey(field)) {
                    fieldsWithoutGrassSet.add(field);
                }
            }
        }
        return fieldsWithoutGrassSet;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        SortedSet<Animal> animalSet = animals.get(position);
        if (animalSet != null && !animalSet.isEmpty()) {
            return animalSet.first();  // return the first animal from the sorted set (or handle differently)
        }
        return null;  // If no animals are at the position
    }


    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>();
        for (SortedSet<Animal> animalSet : animals.values()) {
            elements.addAll(animalSet);
        }
        elements.addAll(grassMap.values());
        return elements;
    }


    public boolean isGrassAt(Vector2d position) {
        return grassMap.containsKey(position);
    }

    public String toString() {
        Boundary currentBounds = getCurrentBounds();
        return mapVisualizer.draw(currentBounds.lowerLeft(), currentBounds.upperRight());
    }

    @Override
    public int getId() {
        return id;
    }

    public Boundary getCurrentBounds() {
        return finalBoundary;
    }
}