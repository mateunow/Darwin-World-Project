package agh.ics.oop.model;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;
    private final Vector2d maxPosition = new Vector2d(4,4);
    private final Vector2d minPosition = new Vector2d(0,0);
    private final Vector2d startPosition = new Vector2d(2, 2);

    public Animal() {
        this.direction = MapDirection.NORTH;
        this.position = startPosition;
    }
    public Animal(MapDirection direction, Vector2d positions){
        this.direction = direction;
        this.position = positions;
    }
    public void setPosition(Vector2d newPosition) {
        if (newPosition.precedes(maxPosition) && newPosition.follows(minPosition)) {
            this.position = newPosition;
            this.direction = MapDirection.NORTH;
        }
        else {
        throw new IllegalArgumentException("Invalid starting position for an animal");
    }
    }

    public String toString() {
        return switch (this.direction) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case WEST -> "<";
            case EAST -> ">";
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }


    public void move(MoveDirection direction, MoveValidator validator) {
        Vector2d potentialNewPosition;
        switch (direction) {
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case FORWARD:
                potentialNewPosition = this.position.add(this.direction.toUnitVector());
                if (validator.canMoveTo(potentialNewPosition)) {
                    this.position = potentialNewPosition;
                }
                break;
            case BACKWARD:
                potentialNewPosition = this.position.subtract(this.direction.toUnitVector());
                if (validator.canMoveTo(potentialNewPosition)) {
                    this.position = potentialNewPosition;
                }
                break;
            default:
                break;

        }
    }

    public Vector2d getPosition(){
        return this.position;
    }
    public MapDirection getDirection() {
        return this.direction;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

}