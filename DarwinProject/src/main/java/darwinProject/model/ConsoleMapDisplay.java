package darwinProject.model;

import darwinProject.model.maps.MapChangeListener;
import darwinProject.model.maps.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener { // czy to jest używane?
    private int updatesCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.println("Current map: " + worldMap.getId());
            System.out.println(message);
            System.out.println(worldMap);
            updatesCount++;
            System.out.println("Updates to this date: " + updatesCount);
        }
    }
}
