package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void correctDirectionsInMoveDirection(){
        //when
        String[] input = {"f", "f","b", "r", "l"};
        List<MoveDirection> expected = List.of(
                MoveDirection.FORWARD,
                MoveDirection.FORWARD,
                MoveDirection.BACKWARD,
                MoveDirection.RIGHT,
                MoveDirection.LEFT);
        ;

        //then
        assertEquals(expected, OptionsParser.parse(input));

    }
    @Test
    void mixValidAndInvalidDirections() {
        //when
        String[] input = {"f", "x", "a", "l", "b"};
        List<MoveDirection> expected = List.of(
                MoveDirection.FORWARD,
                MoveDirection.LEFT,
                MoveDirection.BACKWARD);

        //then
        try {
        assertEquals(expected, OptionsParser.parse(input));
        } catch (IllegalArgumentException e) {
            assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(input));
        }
    }

    @Test
    void onlyInvalidDirections() {
        //when
        String[] input = {"y", "x", "a"};

        //then
        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(input));

    }

    @Test
    void EmptyInput() {
        //when
        String[] input = {""};
        List<MoveDirection> expected = List.of();

        //then
        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(input));

    }

    @Test
    void singleDirection () {
        //when
        String[] input = {"f"};
        List<MoveDirection> expected = List.of(MoveDirection.FORWARD);

        //then
        assertEquals(expected, OptionsParser.parse(input));
    }
}