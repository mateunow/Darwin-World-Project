package darwinProject.presenter;

import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.Animal;
import darwinProject.model.maps.EarthMap;
import darwinProject.model.Vector2d;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
//        AbstractWorldMap map = new RectangularMap(10, 10);
        AbstractWorldMap map = new EarthMap(100,100,10, 2, 20); //TODO change this
        map.registerObservers(presenter);
        List<Vector2d> initialPositions = List.of(new Vector2d(1, 2), new Vector2d(3, 4));
        for (Vector2d position : initialPositions) {
            try {
                map.place(new Animal(position, 7, 50, 30, 20, 0, 3)); //TODO CHANGE THIS TO VARIABLES
            } catch (Exception e) {
                System.err.println("Nie udało się ustawić zwierzęcia na pozycji " + position + ": " + e.getMessage());
            }
        }
        presenter.setWorldMap(map);
        Platform.runLater(() -> presenter.mapChanged(map, "Przykładowa mapa początkowa. Zwierzęta zostaną na swoich pozycjach, pozycje trawy zmienią się."));
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
