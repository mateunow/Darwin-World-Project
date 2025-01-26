package darwinProject.presenter;

import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.Animal;
import darwinProject.model.maps.EarthMap;
import darwinProject.model.Vector2d;
import darwinProject.statistics.SimulationStatistics;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ładowanie pliku FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();

        // Inicjalizacja mapy i ustawienie obserwatorów
        AbstractWorldMap map = new EarthMap(10, 10, 3, 2, 20);
        map.registerObservers(presenter);

// Ustawienie pozycji początkowych zwierząt
        List<Vector2d> initialPositions = List.of(new Vector2d(1, 2), new Vector2d(3, 4));
        for (Vector2d position : initialPositions) {
            try {
                map.place(new Animal(position, 7, 50, 30, 20, 0, 3)); //TODO CHANGE THIS TO VARIABLES
            } catch (Exception e) {
                System.err.println("Nie udało się ustawić zwierzęcia na pozycji " + position + ": " + e.getMessage());
            }
        }

// Powiązanie mapy z prezenterem
        presenter.setWorldMap(map);
        Platform.runLater(() -> presenter.mapChanged(map, "Przykładowa mapa początkowa. TODO???"));

// Uruchomienie okna statystyk
        SimulationStatistics stats = new SimulationStatistics(map);
        stats.showStatisticsWindow();
        // Dodane wywołanie

// Konfiguracja sceny z załadowaniem arkusza stylów CSS
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);

// Konfiguracja okna
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.show();
    }
}
