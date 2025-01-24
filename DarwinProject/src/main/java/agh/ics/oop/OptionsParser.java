package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] args) {
        List<MoveDirection> directions = new ArrayList<>();

        for (String arg : args) {
            switch (arg) {
                case ("forward"):
                case ("f"):
                    directions.add(MoveDirection.FORWARD);
                    break;
                case "b":
                case("backward"):
                    directions.add(MoveDirection.BACKWARD);
                    break;
                case"right":
                case "r":
                    directions.add(MoveDirection.RIGHT);
                    break;
                case "left":
                case "l":
                    directions.add(MoveDirection.LEFT);
                    break;
                default:
                    throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return directions;
    }
}