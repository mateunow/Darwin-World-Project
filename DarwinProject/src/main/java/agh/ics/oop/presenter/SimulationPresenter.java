package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static agh.ics.oop.OptionsParser.parse;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap map;
    @FXML
    private GridPane mapGrid;
    @FXML
    private TextField moveListTextField;
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

    private final int mapMaxHeight = 300;
    private final int mapMaxWidth = 300;

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    public void xyLabel(){
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    public void updateBounds(){
        Boundary boundary = map.getCurrentBounds();
        Vector2d lowerLeft = boundary.lowerLeft();
        Vector2d upperRight = boundary.upperRight();
        xMin = lowerLeft.getX();
        yMin = lowerLeft.getY();
        xMax = upperRight.getX();
        yMax = upperRight.getY();
        mapWidth = xMax - xMin + 1;
        mapHeight = yMax - yMin + 1;
        cellWidth = Math.round(mapMaxWidth/mapWidth);
        cellHeight = Math.round(mapMaxHeight/mapHeight);
        cellHeight = Math.min(cellHeight, cellWidth);
        cellWidth= cellHeight;
    }

    public void columnsFunction(){
        for(int i=0; i<mapWidth; i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for(int i=0; i<mapWidth; i++){
            Label label = new Label(Integer.toString(i + xMin));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }
    }

    public void rowsFunction(){
        for(int i=0; i<mapHeight; i++){
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
        for(int i=0; i<mapHeight; i++){
            Label label = new Label(Integer.toString(yMax - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }
    }

    public void addElements(){
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMax; j >= yMin; j--) {
                Vector2d pos = new Vector2d(i, j);
                if (map.isOccupied(pos)) {
                    mapGrid.add(new Label(map.objectAt(pos).toString()), i - xMin + 1, yMax - j + 1);
                }
                else {
                    mapGrid.add(new Label(" "), i - xMin + 1, yMax - j + 1);
                }
                mapGrid.setHalignment(mapGrid.getChildren().get(mapGrid.getChildren().size() - 1), HPos.CENTER);
            }
        }
    }

    private void drawMap() {
        updateBounds();
        xyLabel();
        columnsFunction();
        rowsFunction();
        addElements();
        mapGrid.setGridLinesVisible(true);
    }

    private void clearGrid(){
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            moveDescriptionLabel.requestFocus();
            clearGrid();
            drawMap();
            moveDescriptionLabel.setText(message);
        });
    }

    @FXML
    private void startSimulation() {
        String moveList = moveListTextField.getText();
        List<MoveDirection> directions = parse(moveList.split(" "));
        List<Vector2d> positions = List.of(new Vector2d(1, 2), new Vector2d(3, 4));

        Stage newStage = new Stage();
        newStage.setTitle("Simulation Window");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane newRoot = loader.load();

            SimulationPresenter newPresenter = loader.getController();
            AbstractWorldMap map = new GrassField(10);
            map.registerObservers(newPresenter);
            newPresenter.setWorldMap(map);

            Simulation simulation = new Simulation(positions, directions, map);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            newPresenter.moveDescriptionLabel.setText("Simulation started with moves: " + moveList);

            new Thread(engine::runSync).start();

            Scene scene = new Scene(newRoot);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
