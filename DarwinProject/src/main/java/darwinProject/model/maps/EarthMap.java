package darwinProject.model.maps;

import darwinProject.enums.MapDirection;
import darwinProject.model.Animal;
import darwinProject.model.Vector2d;
import darwinProject.model.WorldElement;

import java.util.*;


public class EarthMap extends AbstractWorldMap {
    public EarthMap(int height, int width, int startGrassCount, int numberOfPlantsGrownDaily, int energyFromEatingPlant) {
        super(height, width, startGrassCount, numberOfPlantsGrownDaily, energyFromEatingPlant);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }

    @Override
    public void move(Animal animal) {
        Vector2d currentPosition = animal.getPosition();
        MapDirection currentDirection = animal.getDirection();

        // Move the animal
        animal.move(this);
        Vector2d animalNewPosition = animal.getPosition();

        // Remove animal from current position
        SortedSet<Animal> animalsAtCurrentPosition = animals.get(currentPosition);
        if (animalsAtCurrentPosition != null) {
            animalsAtCurrentPosition.remove(animal);
            if (animalsAtCurrentPosition.isEmpty()) {
                animals.remove(currentPosition);
            }
        }

        // Add animal to new position
        animals.computeIfAbsent(animalNewPosition, k -> new TreeSet<>()).add(animal);

        // Notify observers if the position or direction has changed
        if (!animal.getPosition().equals(currentPosition)) {
            notifyObservers("Animal moved from " + currentPosition + " to " + animal.getPosition());
        }
        if (!animal.getDirection().equals(currentDirection)) {
            notifyObservers("Animal turned from " + currentDirection + " to " + animal.getDirection());
        }
    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        SortedSet<Animal> animalsAtPosition = animals.get(position);
        if (animalsAtPosition != null && !animalsAtPosition.isEmpty()) {
            return animalsAtPosition.first();  // Return the strongest animal based on the comparator
        }
        return grassMap.get(position);
    }


    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> worldElements = super.getElements();
        for (SortedSet<Animal> animalSet : animals.values()) {
            worldElements.addAll(animalSet);
        }
        worldElements.addAll(grassMap.values());
        return worldElements;
    }

}
