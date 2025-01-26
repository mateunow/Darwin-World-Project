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
    private TextField startingGrassCountField;
    @FXML
    private TextField energyFromEatingPlantsField;
    @FXML
    private TextField numberOfPlantsGrownDailyField;
    @FXML
    private TextField startingEnergyField;
    @FXML
    private TextField energyReadyToReproduceField;
    @FXML
    private TextField energyToReproduceField;
    @FXML
    private TextField minNumberOfMutationsField;
    @FXML
    private TextField maxNumberOfMutationsField;
    @FXML
    private TextField numberOfGenesField;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button startButton;


    // Metoda do inicjalizacji formularza
    @FXML
    public void initialize() {
        errorMessageLabel.setText(""); // Ukrycie błędów na początku
    }

    // Metoda do uruchomienia symulacji na podstawie wprowadzonych danych
    @FXML
    private void onStartSimulation() {
        try {
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int numAnimals = Integer.parseInt(numAnimalsField.getText());
            int numMoves = Integer.parseInt(numMovesField.getText());
            int startingGrassCount = Integer.parseInt(startingGrassCountField.getText());
            int energyFromEatingPlants = Integer.parseInt(energyFromEatingPlantsField.getText());
            int numberOfPlantsGrownDaily = Integer.parseInt(numberOfPlantsGrownDailyField.getText());
            int startingEnergy = Integer.parseInt(startingEnergyField.getText());
            int energyReadyToReproduce = Integer.parseInt(energyReadyToReproduceField.getText());
            int energyToReproduce = Integer.parseInt(energyToReproduceField.getText());
            int minNumberOfMutations = Integer.parseInt(minNumberOfMutationsField.getText());
            int maxNumberOfMutations = Integer.parseInt(maxNumberOfMutationsField.getText());
            int numberOfGenes = Integer.parseInt(numberOfGenesField.getText());

            // Check if the data is valid
            if (mapWidth <= 0 || mapHeight <= 0 || numAnimals <= 0 || numMoves <= 0 || startingGrassCount <= 0 || energyFromEatingPlants <= 0 || numberOfGenes <= 0
            || numberOfPlantsGrownDaily <= 0 || startingEnergy <= 0 || energyReadyToReproduce <= 0 || minNumberOfMutations <= 0 || maxNumberOfMutations <= 0 || energyToReproduce <= 0) {
                throw new NumberFormatException("Wszystkie dane muszą być dodatnie!");
            }

            // Start the simulation with the gathered parameters
            startNewSimulationWindow(mapWidth, mapHeight, startingGrassCount, energyFromEatingPlants,
                    numberOfPlantsGrownDaily, numAnimals, energyReadyToReproduce, energyToReproduce,
                    minNumberOfMutations, maxNumberOfMutations, numberOfGenes, startingEnergy, numMoves);

        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Błąd: " + e.getMessage());
        }
    }


    // Funkcja uruchamiająca nowe okno z symulacją
    private void startNewSimulationWindow(int mapWidth, int mapHeight, int startingGrassCount, int energyFromEatingPlants,
                                          int numberOfPlantsGrownDaily, int numAnimals, int energyReadyToReproduce,
                                          int energyToReproduce, int minNumberOfMutations, int maxNumberOfMutations,
                                          int numberOfGenes, int startingEnergy, int numMoves) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Simulation Window");

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane newRoot = loader.load();
            SimulationPresenter newPresenter = loader.getController();

            // Create map and simulation with new parameters
            AbstractWorldMap map = new EarthMap(mapWidth, mapHeight, startingGrassCount, numberOfPlantsGrownDaily, energyFromEatingPlants);
            map.registerObservers(newPresenter);
            newPresenter.setWorldMap(map);

            Simulation simulation = new Simulation(mapWidth, mapHeight, startingGrassCount, energyFromEatingPlants,
                    numberOfPlantsGrownDaily, numAnimals, energyReadyToReproduce, energyToReproduce,
                    minNumberOfMutations, maxNumberOfMutations, numberOfGenes, startingEnergy);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));

            newPresenter.moveDescriptionLabel.setText("Symulacja rozpoczęta z parametrami: " + numMoves + " moves.");

            new Thread(engine::runSync).start();

            Scene scene = new Scene(newRoot);
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
