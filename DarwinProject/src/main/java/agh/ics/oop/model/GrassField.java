package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grassMap = new HashMap<>();
    private final int maxGrassPosition;
    private final Vector2d lowerLeft = new Vector2d(Integer.MIN_VALUE,Integer.MIN_VALUE);
    private final Vector2d upperRight = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public GrassField(int grassCount) {
        this.maxGrassPosition = (int) sqrt(grassCount * 10);


        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxGrassPosition, maxGrassPosition, grassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grassMap.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement object = super.objectAt(position);
        if(object != null) {return object;}
        return grassMap.get(position);

    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> worldElements = super.getElements();
        worldElements.addAll(grassMap.values());
        return worldElements;
    }

    @Override
    public Boundary getCurrentBounds(){
        Vector2d bottomLeft = upperRight;
        Vector2d topRight = lowerLeft;
        List<WorldElement> elements = this.getElements();

        for (WorldElement element: elements) {
            Vector2d position = element.getPosition();
            if (!bottomLeft.precedes(position)) {
                bottomLeft = bottomLeft.lowerLeft(position);
            }
            if (!topRight.follows(position)) {
                topRight = topRight.upperRight(position);
            }
        }
         return new Boundary(bottomLeft, topRight);
    }

}

