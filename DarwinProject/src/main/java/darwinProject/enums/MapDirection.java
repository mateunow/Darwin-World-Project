package darwinProject.enums;

import darwinProject.model.Vector2d;

public enum MapDirection{
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST,;
    private final Vector2d north = new Vector2d(0,1);
    private final Vector2d northEast = new Vector2d(1,1);
    private final Vector2d east = new Vector2d(1,0);
    private final Vector2d southEast = new Vector2d(1,-1);
    private final Vector2d south = new Vector2d(0,-1);
    private final Vector2d southWest = new Vector2d(-1,-1);
    private final Vector2d west = new Vector2d(-1,0);
    private final Vector2d northWest = new Vector2d(-1,1);

    @Override
    public String toString() {
        return switch(this) {
            case NORTH -> "Północ";
            case NORTHEAST -> "Północny wschód";
            case EAST ->"Wschód";
            case SOUTHEAST -> "Południowy wschód";
            case SOUTH -> "Południe";
            case SOUTHWEST -> "Południowy zachód";
            case WEST -> "Zachód";
            case NORTHWEST -> "Północny zachód";
        };
    }

    public MapDirection turn (int turnCount) {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal() + turnCount) % values.length];
    }

    public Vector2d toUnitVector(){
     return switch(this){
         case NORTH -> north;
         case SOUTH -> south;
         case EAST -> east;
         case WEST -> west;
         case NORTHEAST -> northEast;
         case NORTHWEST -> northWest;
         case SOUTHEAST -> southEast;
         case SOUTHWEST -> southWest;
     };
    }
    public int getAngle() {
        return this.ordinal() * 45;
    }
}