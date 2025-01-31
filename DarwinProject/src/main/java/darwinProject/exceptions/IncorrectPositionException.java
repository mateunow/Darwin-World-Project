package darwinProject.exceptions; // pakiet specjalnie na 1 wyjątek?

import darwinProject.model.Vector2d;

public class IncorrectPositionException extends Exception { // czy to jest używane?
    private final Vector2d position;

    public IncorrectPositionException(Vector2d position) {
        super("Position " + position + " is not correct.");
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }
}
