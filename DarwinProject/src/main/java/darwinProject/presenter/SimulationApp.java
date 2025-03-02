package darwinProject.presenter;

import darwinProject.Simulation;
import darwinProject.enums.AnimalType;
import darwinProject.enums.MapType;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SimulationApp extends Application {

    private SimulationSettingsWindow settingsWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Open the settings window
        FXMLLoader settingsLoader = new FXMLLoader();
        settingsLoader.setLocation(getClass().getClassLoader().getResource("simulationSettings.fxml"));
        BorderPane settingsRoot = settingsLoader.load();
        settingsWindow = settingsLoader.getController();

        // Show the settings window
        Scene settingsScene = new Scene(settingsRoot);
        Stage settingsStage = new Stage();
        settingsStage.setScene(settingsScene);
        settingsStage.setTitle("Simulation Settings");
        settingsStage.show();

        // Wait for user input and then close settings window
        settingsStage.setOnCloseRequest(event -> {
            SimulationData data = settingsWindow.getSimulationData();
            if (data != null) {
                try {
                    startSimulation(primaryStage, data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Show error message or do nothing if data is invalid
            }
        });
    }


    private void startSimulation(Stage primaryStage, SimulationData data) throws IOException {
        // Create a map with the received data
        AbstractWorldMap map = new EarthMap(
                data.mapWidth, data.mapHeight, data.startingGrassCount, data.energyFromEatingPlants,
                data.numberOfPlantsGrownDaily);
        map.registerObservers(new SimulationPresenter());

        // Set up initial positions for animals, use the number of animals from the input
        List<Vector2d> initialPositions = new ArrayList<>();
        for (int i = 0; i < data.numAnimals; i++) {
            initialPositions.add(new Vector2d(i, i)); // Customize animal positions if needed
        }

        // Place animals on the map
        for (Vector2d position : initialPositions) {
            try {
                map.place(new Animal(position, 7, 50, 30, 20, 0, 3));
            } catch (Exception e) {
                System.err.println("Error placing animal: " + e.getMessage());
            }
        }

        // Create the simulation window and start the simulation
        SimulationPresenter presenter = new SimulationPresenter();
        presenter.setWorldMap(map);
        SimulationPresenter finalPresenter = presenter;
        Platform.runLater(() -> finalPresenter.mapChanged(map, "Initial map setup"));

        // Load the simulation FXML and setup scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        presenter = loader.getController();


        // Uruchomienie symulacji
        startSimulation(map, presenter);

        // Uruchomienie okna statystyk
        SimulationStatistics stats = new SimulationStatistics(map);
        stats.showStatisticsWindow();

        // Konfiguracja sceny z załadowaniem arkusza stylów CSS

        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.setTitle("Simulation app");
        primaryStage.show();
    }

    private void startSimulation(AbstractWorldMap map, SimulationPresenter presenter) {
        // Tworzymy symulację
        Simulation simulation = new Simulation(10, 10, 3, 2, 20, 20, 50, 30, 0, 3, 7, 50, AnimalType.Animal, MapType.EarthMap);
        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        // Uruchamiamy symulację asynchronicznie
        new Thread(() -> {
            engine.runAsync(); // Uruchomienie symulacji w tle
        }).start();
    }
}
