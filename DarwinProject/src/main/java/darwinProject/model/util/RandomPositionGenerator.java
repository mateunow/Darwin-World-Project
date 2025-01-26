package darwinProject.model.util;

import darwinProject.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int grassCount;
    private final List<Vector2d> positions = new ArrayList<>();

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.grassCount = grassCount;

        // Generate all possible positions
        List<Vector2d> allPositions = new ArrayList<>();
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }

        // Generate unique random positions for grass
        generateGrassPositions(allPositions);
    }

    public RandomPositionGenerator(List<Vector2d> positions, int grassCount) {
        this.grassCount = grassCount;
        generateGrassPositions(new ArrayList<>(positions));
    }

    private void generateGrassPositions(List<Vector2d> availablePositions) {
        // Shuffle and pick the first `grassCount` unique positions
        Collections.shuffle(availablePositions);
        for (int i = 0; i < Math.min(grassCount, availablePositions.size()); i++) {
            positions.add(availablePositions.get(i));
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < positions.size();
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more grass positions available.");
                }
                return positions.get(index++);
            }
        };
    }
}

