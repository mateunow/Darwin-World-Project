package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener{
    private int updatesCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.println("Obecna mapa: " + worldMap.getId());
            System.out.println(message);
            System.out.println(worldMap.toString());
            updatesCount++;
            System.out.println("Updates to this date: " + updatesCount);
        }
    }
}
