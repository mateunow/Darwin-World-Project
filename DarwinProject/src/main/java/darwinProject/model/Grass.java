package darwinProject.model;

public class Grass implements WorldElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "\\uD83C\\uDF31"; // Unicode dla kępki trawy 🌱
    }
}
