package darwinProject.presenter;

import darwinProject.Simulation;
import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.Animal;
import darwinProject.model.Vector2d;
import darwinProject.model.maps.EarthMap;
import darwinProject.statistics.SimulationStatistics;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Hello World!");

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

        // Uruchomienie symulacji
        startSimulation(map, presenter);

        // Uruchomienie okna statystyk
        SimulationStatistics stats = new SimulationStatistics(map);
        stats.showStatisticsWindow();

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

    private void startSimulation(AbstractWorldMap map, SimulationPresenter presenter) {
        // Tworzymy symulację
        Simulation simulation = new Simulation(10, 10, 3, 2, 20, 20, 50, 30, 0, 3, 7, 50, presenter);
        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        // Uruchamiamy symulację asynchronicznie
        new Thread(() -> {
            engine.runAsync(); // Uruchomienie symulacji w tle
        }).start();
    }
}
