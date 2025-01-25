package darwinProject.presenter;

import darwinProject.Simulation;
import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.maps.EarthMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

import java.util.List;

public class SimulationSettingsWindow {
    @FXML
    private Label moveDescriptionLabel;
    @FXML
    private TextField mapWidthField;
    @FXML
    private TextField mapHeightField;
    @FXML
    private TextField numAnimalsField;
    @FXML
    private TextField numMovesField;
    @FXML
    private Button startButton;
    @FXML
    private Label errorMessageLabel;

    // Metoda do inicjalizacji formularza
    @FXML
    public void initialize() {
        errorMessageLabel.setText(""); // Ukrycie błędów na początku
    }

    // Metoda do uruchomienia symulacji na podstawie wprowadzonych danych
    @FXML
    private void onStartSimulation() {
        try {
            // Odczytanie danych z pól tekstowych
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int numAnimals = Integer.parseInt(numAnimalsField.getText());
            int numMoves = Integer.parseInt(numMovesField.getText());

            // Sprawdzanie, czy dane są poprawne
            if (mapWidth <= 0 || mapHeight <= 0 || numAnimals <= 0 || numMoves <= 0) {
                throw new NumberFormatException("Wszystkie dane muszą być dodatnie!");
            }

            // Stworzenie obiektu symulacji i rozpoczęcie nowego okna
            startNewSimulationWindow(mapWidth, mapHeight, numAnimals, numMoves);
        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Błąd: " + e.getMessage());
        }
    }

    // Funkcja uruchamiająca nowe okno z symulacją
    private void startNewSimulationWindow(int mapWidth, int mapHeight, int numAnimals, int numMoves) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Simulation Window");

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));

            BorderPane newRoot = loader.load();

            SimulationPresenter newPresenter = loader.getController();

            // Stwórz mapę i symulację z wprowadzonymi danymi
            AbstractWorldMap map = new EarthMap(mapWidth, mapHeight, numAnimals, 2, numMoves); // Tutaj wstawiamy odpowiednie parametry
            map.registerObservers(newPresenter);
            newPresenter.setWorldMap(map);

            Simulation simulation = new Simulation(mapWidth, mapHeight, numMoves, numAnimals, 50, 20, 0, 3, 7, 50,1,1);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));

            newPresenter.moveDescriptionLabel.setText("Symulacja rozpoczęta!");

            // Uruchomienie symulacji w osobnym wątku
            new Thread(engine::runSync).start();

            Scene scene = new Scene(newRoot);
            newStage.setScene(scene);
            newStage.show();
            System.out.println("poszło");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
