package darwinProject.model.maps;

import darwinProject.enums.MapDirection;
import darwinProject.model.*;
import darwinProject.model.util.Boundary;
import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    private final HashSet<MapChangeListener> observers = new HashSet<>();
    protected final int id = this.hashCode();


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

    @Override
    public void move(Animal animal) {
        Vector2d currentPosition = animal.getPosition();
        MapDirection currentDirection = animal.getDirection();

        animal.move( this);
        animals.remove(currentPosition);
        animals.put(animal.getPosition(), animal);

        if (!animal.getPosition().equals(currentPosition)) {
            notifyObservers("Animal moved from " + currentPosition + " to " + animal.getPosition());
        }
        if (!animal.getDirection().equals(currentDirection)) {
            notifyObservers("Animal turned from " + currentDirection + " to " + animal.getDirection());
        }
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException{
        Vector2d position = animal.getPosition();
        if (canMoveTo(position)) {
            animals.put(position, animal);
            notifyObservers("Animal placed at " + animal.getPosition() );
        }
        else {
        throw new IncorrectPositionException(position);
    }}
    public abstract void eatPlants(List<Animal> list);
    public Set<Vector2d> findFieldsWithoutGrass() {
        Set<Vector2d> positions = new HashSet<>();
        Boundary boundary = getCurrentBounds();
        int width = boundary.upperRight().getY();
        int height = boundary.upperRight().getX();

        for (int i = 0; i < width; i++ ) {
            for (int j = 0; j < height; j++) {
                positions.add(new Vector2d(i, j));
            }
        }
        return positions;
    }

    public abstract void generateNewGrassPositions();

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    public List<WorldElement> getElements(){
        return new ArrayList<>(animals.values());
    }


    public abstract Boundary getCurrentBounds();

    public String toString() {
        Boundary currentBounds = getCurrentBounds();
        return mapVisualizer.draw(currentBounds.lowerLeft(), currentBounds.upperRight());
    }

    @Override
    public int getId() {
        return id;
    }
}
