package agh.ics.oop.model;


import agh.ics.oop.model.util.Boundary;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d upperRight;
    private final Vector2d lowerLeft = new Vector2d(0, 0);
    private final Boundary finalBounds;

    public RectangularMap(int width, int height) {
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.finalBounds = new Boundary(lowerLeft, upperRight);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && position.precedes(upperRight) && position.follows(lowerLeft);
    }

    @Override
    public Boundary getCurrentBounds(){
        return finalBounds;
    }

}




