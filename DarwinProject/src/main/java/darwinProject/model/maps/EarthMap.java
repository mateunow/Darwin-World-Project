package darwinProject.model.maps;

import darwinProject.enums.MapDirection;
import darwinProject.model.Animal;
import darwinProject.model.Grass;
import darwinProject.model.Vector2d;
import darwinProject.model.WorldElement;
import darwinProject.model.util.Boundary;
import darwinProject.model.util.RandomPositionGenerator;

import java.util.*;


public class EarthMap extends AbstractWorldMap {
    //TODO zoptymalizuj tą klasę bo pewnie da się lepiej
    private final Vector2d upperRight;
    private final Vector2d lowerLeft = new Vector2d(0,0);
    private final Boundary finalBoundary;
    private final Map<Vector2d, Grass> grassMap = new HashMap<>();
    private Set<Vector2d> fieldsWithoutGrass = new HashSet<>();
    private final Integer numberOfPlantsGrownDaily;
    private final Integer energyFromEatingPlant;


    public EarthMap(int height, int width, int startGrassCount, int numberOfPlantsGrownDaily, int energyFromEatingPlant) {
        this.upperRight = new Vector2d(width - 1,height - 1);
        this.finalBoundary = new Boundary(lowerLeft, upperRight);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, startGrassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
        }
        this.fieldsWithoutGrass = findFieldsWithoutGrass();
        this.numberOfPlantsGrownDaily = numberOfPlantsGrownDaily;
        this.energyFromEatingPlant = energyFromEatingPlant;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
        //TODO change this from precedes to other method that does not check both values in Vector2d
        // Dodać walidator ten do animala i jak będzie próbowało wyjść w górę albo w dół to przesuwać
    }

    @Override
    public void eatPlants(List<Animal> list) {

    }


    @Override
    public void move(Animal animal) {
        Vector2d currentPosition = animal.getPosition();
        MapDirection currentDirection = animal.getDirection();

        animal.move( this);
        Vector2d animalNewPosition = animal.getPosition();
        if (grassMap.containsKey(animalNewPosition)) {
            grassMap.remove(animalNewPosition);
            animal.addEnergy(energyFromEatingPlant); //TODO ZAMIEŃ TO NA WŁAŚCIWĄ ENERGIĘ
            fieldsWithoutGrass.add(currentPosition);
        }
        animals.remove(currentPosition);
        animals.put(animalNewPosition, animal);

        if (!animal.getPosition().equals(currentPosition)) {
            notifyObservers("Animal moved from " + currentPosition + " to " + animal.getPosition());
        }
        if (!animal.getDirection().equals(currentDirection)) {
            notifyObservers("Animal turned from " + currentDirection + " to " + animal.getDirection());
        }
    }

    @Override
    public void generateNewGrassPositions() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(new ArrayList<>(fieldsWithoutGrass), numberOfPlantsGrownDaily);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
            fieldsWithoutGrass.remove(grassPosition);
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement object = super.objectAt(position);
        if(object != null) {return object;}
        return grassMap.get(position);

    }

    @Override
    public Set<Vector2d> findFieldsWithoutGrass() {
        Boundary boundary = getCurrentBounds();
        int width = boundary.upperRight().getY();
        int height = boundary.upperRight().getX();

        Set<Vector2d> fieldsWithoutGrassSet = new HashSet<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d field = new Vector2d(i, j);
                if (!grassMap.containsKey(field)) {
                    fieldsWithoutGrassSet.add(field);
                }
            }
        }
        return fieldsWithoutGrassSet;
    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> worldElements = super.getElements();
        worldElements.addAll(grassMap.values());
        return worldElements;
    }

    @Override
    public Boundary getCurrentBounds() {
        return finalBoundary;
    }

    public Set<Vector2d> getFieldsWithoutGrass() {
        return fieldsWithoutGrass;
    }

    public int getNumberOfPlantsGrownDaily(){
        return numberOfPlantsGrownDaily;
    }
}