package darwinProject.presenter;

import darwinProject.model.Vector2d;
import darwinProject.model.maps.MapChangeListener;
import darwinProject.model.maps.WorldMap;
import darwinProject.model.util.Boundary;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.geometry.HPos;
import darwinProject.Simulation;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;

public class SimulationPresenter implements MapChangeListener {

    private WorldMap map;
    protected Simulation simulation;
    SimulationEngine engine;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label moveDescriptionLabel;


    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;
    private int mapWidth;
    private int mapHeight;

    private int cellWidth = 50;
    private int cellHeight = 50;

    // Set the world map
    public void setWorldMap(WorldMap map) {
        this.map = map;
        map.registerObservers(this);

        drawMap();
    }
    public void startSimulation(){
        // Create a new thread to run the simulation
        new Thread(() -> {
            int i = 0;
            while(true) {
                // Run the simulation logic
                System.out.println("day " + i);
                i++;

                // Update world
                simulation.getWorldMap().handleMovement();
                simulation.getWorldMap().eatPlants();
                simulation.getWorldMap().handlePlantConsumption();
                simulation.getWorldMap().handleReproduction();
                simulation.getWorldMap().generateNewGrassPositions();

                // Update the UI
                int finalI = i;
                Platform.runLater(() -> {
                    mapChanged(simulation.getWorldMap(), "Day " + finalI); // Assuming the message you want to show on the label is the day number
                });

                try {
                    // Introduce a small delay to simulate the passage of time (and avoid freezing the UI)
                    Thread.sleep(1000);  // 1 second delay, adjust as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Update map boundaries based on the map data
    public void updateBounds() {
        Boundary boundary = map.getCurrentBounds();
        Vector2d lowerLeft = boundary.lowerLeft();
        Vector2d upperRight = boundary.upperRight();
        xMin = lowerLeft.getX();
        yMin = lowerLeft.getY();
        xMax = upperRight.getX();
        yMax = upperRight.getY();
        mapWidth = xMax - xMin + 1;
        mapHeight = yMax - yMin + 1;
        int mapMaxWidth = 300;
        cellWidth = Math.round(mapMaxWidth / mapWidth);
        int mapMaxHeight = 300;
        cellHeight = Math.round(mapMaxHeight / mapHeight);
        cellHeight = Math.min(cellHeight, cellWidth);
        cellWidth = cellHeight;
    }

    // Set the column function to populate columns in the GridPane
    public void columnsFunction() {
        for (int i = 0; i < mapWidth; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int i = 0; i < mapWidth; i++) {
            Label label = new Label(Integer.toString(i + xMin));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }
    }

    // Set the row function to populate rows in the GridPane
    public void rowsFunction() {
        for (int i = 0; i < mapHeight; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }

        // Wrap the UI update code inside Platform.runLater
        Platform.runLater(() -> {
            for (int i = 0; i < mapHeight; i++) {
                Label label = new Label(Integer.toString(yMax - i));
                GridPane.setHalignment(label, HPos.CENTER);
                mapGrid.add(label, 0, i + 1);
            }
        });
    }


    // Add elements (animals, plants) to the map
    private void addElements() {
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMax; j >= yMin; j--) {
                Vector2d pos = new Vector2d(i, j);

                // Wrap UI updates in Platform.runLater
                int finalI = i;
                int finalJ = j;
                Platform.runLater(() -> {
                    Label label;
                    if (map.isOccupied(pos)) {
                        label = new Label(map.objectAt(pos).toString());
                    } else {
                        label = new Label(" ");
                    }
                    mapGrid.add(label, finalI - xMin + 1, yMax - finalJ + 1);
                    mapGrid.setHalignment(label, HPos.CENTER);  // Set the label's alignment to center
                });
            }
        }
    }



    // Draw the map with the updated elements and grid layout
    private void drawMap() {
        updateBounds();
        columnsFunction();
        rowsFunction();
        addElements();
        mapGrid.setGridLinesVisible(true);
    }

    // Clear the grid before redrawing
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    // Event triggered when the map changes
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        setWorldMap(worldMap);  // Update the map reference

        // Run UI updates on the JavaFX Application Thread
        Platform.runLater(() -> {
            moveDescriptionLabel.requestFocus();
            clearGrid();         // Clear the current grid
            drawMap();           // Re-draw the map
            moveDescriptionLabel.setText(message);  // Update the move description
        });
    }



}

