package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private final List<Vector2d> positions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        this.positions = new ArrayList<>();

        generatePositions();
    }

    private void generatePositions() {
        //tworzę listę wszystkich możliwych pozycji
        List<Vector2d> allPositions = new ArrayList<>();
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }

        // Tasowanie unikalnych pozycji odbywa się tylko raz więc jest deterministyczne
        Collections.shuffle(allPositions);
        for (int i = 0; i < grassCount; i++) {
            positions.add(allPositions.get(i));
            //po potasowaniu dodaje losowe pozycje do listy positions
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < grassCount;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Nie ma następnych wektorów");
                }
                Vector2d position = positions.get(index);
                index++;
                return position;
            }
        };
    }
}
