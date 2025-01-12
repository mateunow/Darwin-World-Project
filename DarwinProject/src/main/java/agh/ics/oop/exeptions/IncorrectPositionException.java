package agh.ics.oop.exeptions;

import agh.ics.oop.model.Vector2d;

public class IncorrectPositionException extends Exception {
    private final Vector2d position;

    public IncorrectPositionException(Vector2d position) {
        super("Position " + position+ " is not correct.");
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }
}
