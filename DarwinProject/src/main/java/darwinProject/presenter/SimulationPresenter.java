package darwinProject.presenter;

import darwinProject.enums.AnimalType;
import darwinProject.enums.MapType;
import darwinProject.model.Vector2d;
import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.maps.EarthMap;
import darwinProject.model.maps.MapChangeListener;
import darwinProject.model.maps.WorldMap;
import darwinProject.model.util.Boundary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.geometry.HPos;
import darwinProject.Simulation;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationPresenter implements MapChangeListener {

    private WorldMap map;
    protected Simulation simulation;
    SimulationEngine engine;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label moveDescriptionLabel;


    private int xMin, yMin, xMax, yMax, mapWidth, mapHeight;
    private int cellWidth = 50;
    private int cellHeight = 50;
    public void setWorldMap(WorldMap map) {
        this.map = map;
        map.registerObservers(this);
        drawMap();
    }
    

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
        cellWidth = mapMaxWidth / mapWidth;
        int mapMaxHeight = 300;
        cellHeight = (mapMaxHeight / mapHeight);
        cellHeight = Math.min(cellHeight, cellWidth);
        cellWidth = cellHeight;
    }

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
                    mapGrid.setHalignment(label, HPos.CENTER); 
                });
            }
        }
    }



    
    private void drawMap() {
        updateBounds();
        columnsFunction();
        rowsFunction();
        addElements();
        mapGrid.setGridLinesVisible(true);
    }


    // Funkcja do wyświetlenia współrzędnych
    private void xyLabel() {
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        Label label = new Label("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    // Funkcja czyszcząca grid przed rysowaniem nowego widoku
    private void clearGrid() {
        mapGrid.getChildren().clear();

        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }


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


    // Metoda wywołująca updateMap z Simulation co sekundę
    public void updateMap() {
        Platform.runLater(() -> {
            clearGrid();
            drawMap();  // Rysowanie mapy
        });
    }

    // Uruchomienie nowego okna symulacji
    public void startSimulation() {
        // Tworzymy nowe okno
        Stage newStage = new Stage();
        newStage.setTitle("Simulation Window");

        try {
            // Ładujemy FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("okno.fxml"));
            BorderPane newRoot = loader.load();

            // Pobieramy kontroler
            SimulationPresenter newPresenter = loader.getController();
            AbstractWorldMap map = new EarthMap(100, 100, 10, 2, 20);
            map.registerObservers(newPresenter);
            newPresenter.setWorldMap(map);

            // Tworzymy i uruchamiamy symulację
            Simulation simulation = new Simulation(100, 100, 20, 20, 5, 5, 50, 20, 0, 3, 7, 50, AnimalType.Animal, MapType.EarthMap);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            newPresenter.moveDescriptionLabel.setText("Simulation started");

            new Thread(engine::runSync).start();

            // Ustawienie interwału dla odświeżania mapy
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                newPresenter.updateMap(); // Co sekundę wywołujemy metodę aktualizacji
            }, 0, 1, TimeUnit.SECONDS); // Opóźnienie 0, interwał 1 sekunda

            // Pokazujemy nowe okno
            Scene scene = new Scene(newRoot);
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

