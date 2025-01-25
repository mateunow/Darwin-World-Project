package darwinProject.model.util;

import darwinProject.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int grassCount;
    private final List<Vector2d> positions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        List<Vector2d> allPositions = new ArrayList<>();
        this.grassCount = grassCount;
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }

        this.positions = allPositions;

        generateGrassPositions(allPositions, grassCount);
    }

    public RandomPositionGenerator(ArrayList<Vector2d> positions, int numberOfGrassFieldsToAdd) {
        this.grassCount = numberOfGrassFieldsToAdd;
        this.positions = positions;
        generateGrassPositions(positions, numberOfGrassFieldsToAdd);
    }
    public void generateGrassPositions(List<Vector2d> positions, Integer grassCount) {
        if (positions.size() < grassCount) {
            positions.addAll(positions);
        }
        else{
        Collections.shuffle(positions);
        for (int i = 0; i < grassCount; i++) {
            positions.add(positions.get(i));
        }
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
