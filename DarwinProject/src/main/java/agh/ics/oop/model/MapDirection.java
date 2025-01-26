package agh.ics.oop.model;

public enum MapDirection{
    NORTH, SOUTH, WEST, EAST;
    private final Vector2d north = new Vector2d(0,1);
    private final Vector2d south = new Vector2d(0,-1);
    private final Vector2d west = new Vector2d(-1,0);
    private final Vector2d east = new Vector2d(1,0);

    @Override
    public String toString() {
        return switch(this) {
            case EAST ->"Wschód";
            case WEST -> "Zachód";
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            default -> throw new IllegalArgumentException("Nieznany kierunek");
        };


    }
    public MapDirection next(){

        //w next i previous można było w jednej linijce przez 1%4 = NORTH, 2%4 = EAST itd i póżniej łatwe obliczenia
        return switch(this) {
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case NORTH ->EAST;
            default -> throw new IllegalArgumentException("Nieznany kierunek");
        };
    }
    public MapDirection previous(){
        return switch(this){
            case EAST -> NORTH;
            case WEST -> SOUTH;
            case NORTH -> WEST;
            case SOUTH -> EAST;
            default -> throw new IllegalArgumentException("Nieznany kierunek");
        };
    }


    public Vector2d toUnitVector(){
     return switch(this){
         case NORTH -> north;
         case SOUTH -> south;
         case EAST -> east;
         case WEST -> west;
         default -> throw new IllegalArgumentException("Nieznany kierunek");
     };
    }
}