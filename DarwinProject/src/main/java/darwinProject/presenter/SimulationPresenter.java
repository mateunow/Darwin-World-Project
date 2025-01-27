package darwinProject.presenter;

import darwinProject.Simulation;
import darwinProject.model.*;
import darwinProject.model.maps.*;
import darwinProject.model.util.Boundary;
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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationPresenter implements MapChangeListener {

    private WorldMap map;
    private int xMin, yMin, xMax, yMax, mapWidth, mapHeight;
    private int cellWidth = 50;
    private int cellHeight = 50;
    private final int mapMaxHeight = 300;
    private final int mapMaxWidth = 300;

    @FXML
    private GridPane mapGrid;  // Przycisk widoku mapy (GridPane)
    @FXML
    private TextField moveListTextField;  // Pole tekstowe do wprowadzenia listy ruchów
    @FXML
    Label moveDescriptionLabel;  // Etykieta z opisem ruchów

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    // Aktualizacja wymiarów mapy
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
        cellWidth = Math.round(mapMaxWidth / mapWidth);
        cellHeight = Math.round(mapMaxHeight / mapHeight);
        cellHeight = Math.min(cellHeight, cellWidth);
        cellWidth = cellHeight;
    }

    // Dodawanie etykiet do kolumn
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

    // Dodawanie etykiet do wierszy
    public void rowsFunction() {
        for (int i = 0; i < mapHeight; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        }
        for (int i = 0; i < mapHeight; i++) {
            Label label = new Label(Integer.toString(yMax - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }
    }

    // Dodawanie elementów (zwierząt, roślin itp.) do mapy
    public void addElements() {
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMax; j >= yMin; j--) {
                Vector2d pos = new Vector2d(i, j);
                Label cellLabel = null;

                if (map.isOccupied(pos)) {
                    Object obj = map.objectAt(pos);

                    if (obj instanceof Animal) {
                        Animal animal = (Animal) obj;
                        cellLabel = new Label(animal.toString()); // Strzałka z energią

                        int energy = animal.getEnergy();  // Uzyskujemy energię zwierzęcia
                        if (energy < 20) {
                            cellLabel.setStyle("-fx-text-fill: red;");
                        } else if (energy < 50) {
                            cellLabel.setStyle("-fx-text-fill: orange;");
                        } else {
                            cellLabel.setStyle("-fx-text-fill: green;");
                        }

                        // Zdarzenie kliknięcia na komórkę z zwierzęciem
                        cellLabel.setOnMouseClicked(event -> {
                            moveDescriptionLabel.setText("Animal at " + pos + " has energy: " + energy);
                        });

                    } else if (obj instanceof Grass) {
                        cellLabel = new Label("🌱"); // Przykład dla trawy
                        // Zdarzenie kliknięcia na komórkę z trawą
                        cellLabel.setOnMouseClicked(event -> {
                            moveDescriptionLabel.setText("Grass at " + pos);
                        });
                    }
                } else {
                    cellLabel = new Label(" ");  // Pusta przestrzeń
                }

                mapGrid.add(cellLabel, i - xMin + 1, yMax - j + 1);
                mapGrid.setHalignment(cellLabel, HPos.CENTER);
            }
        }
    }

    // Funkcja odpowiedzialna za rysowanie mapy
    private void drawMap() {
        updateBounds();
        xyLabel();
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

    // Zmodyfikowana funkcja mapChanged z wywołaniem do rysowania nowego widoku mapy
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

    // Metoda wywołująca updateMap z Simulation co sekundę
    public void updateMap() {
        Platform.runLater(() -> {
            clearGrid();
            drawMap();  // Rysowanie mapy
        });
    }

    // Uruchomienie nowego okna symulacji
    public void startSimulation() {
        String moveList = moveListTextField.getText();
        List<Vector2d> positions = List.of(new Vector2d(1, 2), new Vector2d(3, 4));

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
            Simulation simulation = new Simulation(100, 100, 20, 20, 5, 5, 50, 20, 0, 3, 7, 50, newPresenter);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            newPresenter.moveDescriptionLabel.setText("Simulation started with moves: " + moveList);

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
