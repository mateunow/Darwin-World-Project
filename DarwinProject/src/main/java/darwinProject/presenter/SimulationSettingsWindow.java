package darwinProject.presenter;

import darwinProject.Simulation;
import darwinProject.enums.AnimalTypes;
import darwinProject.enums.MapType;
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
    private Button startButton;
    @FXML
    private Label errorMessageLabel;

    // Method to initialize the form
    @FXML
    public void initialize() {
        errorMessageLabel.setText(""); // Hide errors initially
    }

    // Method to start the simulation based on user input
    @FXML
    private void onStartSimulation() {
        try {
            // Get values from all the fields
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int numAnimals = Integer.parseInt(numAnimalsField.getText());
            int numMoves = Integer.parseInt(numMovesField.getText());

            // New parameters related to plants and animals
            int startingGrassCount = Integer.parseInt(startingGrassCountField.getText());
            int energyFromEatingPlants = Integer.parseInt(energyFromEatingPlantsField.getText());
            int numberOfPlantsGrownDaily = Integer.parseInt(numberOfPlantsGrownDailyField.getText());
            int startingEnergy = Integer.parseInt(startingEnergyField.getText());
            int energyReadyToReproduce = Integer.parseInt(energyReadyToReproduceField.getText());
            int energyToReproduce = Integer.parseInt(energyToReproduceField.getText());
            int minNumberOfMutations = Integer.parseInt(minNumberOfMutationsField.getText());
            int maxNumberOfMutations = Integer.parseInt(maxNumberOfMutationsField.getText());
            int numberOfGenes = Integer.parseInt(numberOfGenesField.getText());

            // Check that values are within valid ranges
            if (mapWidth < 1 || mapWidth > 10001 || mapHeight < 1 || mapHeight > 10001 ||
                    numAnimals < 1 || numAnimals > 10001 || numMoves < 1 || numMoves > 10001 ||
                    startingGrassCount < 1 || startingGrassCount > 10001 || energyFromEatingPlants < 1 || energyFromEatingPlants > 10001 ||
                    numberOfPlantsGrownDaily < 1 || numberOfPlantsGrownDaily > 10001 || startingEnergy < 1 || startingEnergy > 10001 ||
                    energyReadyToReproduce < 1 || energyReadyToReproduce > 10001 || energyToReproduce < 1 || energyToReproduce > 10001 ||
                    minNumberOfMutations < 0 || minNumberOfMutations > 10001 || maxNumberOfMutations < 0 || maxNumberOfMutations > 10001 ||
                    numberOfGenes < 1 || numberOfGenes > 10001) {
                errorMessageLabel.setText("Błąd: Wszystkie dane muszą być liczbami od 1 do 10001!");
                return;
            }

            // Start a new simulation window
            startNewSimulationWindow(mapWidth, mapHeight, numAnimals, numMoves, startingGrassCount, energyFromEatingPlants,
                    numberOfPlantsGrownDaily, startingEnergy, energyReadyToReproduce, energyToReproduce, minNumberOfMutations,
                    maxNumberOfMutations, numberOfGenes);

        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Błąd: Proszę wprowadzić poprawne liczby!");
        }
    }

    // Create a new simulation window with the given parameters
    private void startNewSimulationWindow(int mapWidth, int mapHeight, int numAnimals, int numMoves,
                                          int startingGrassCount, int energyFromEatingPlants, int numberOfPlantsGrownDaily,
                                          int startingEnergy, int energyReadyToReproduce, int energyToReproduce,
                                          int minNumberOfMutations, int maxNumberOfMutations, int numberOfGenes) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Simulation Window");

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane newRoot = loader.load();

            SimulationPresenter newPresenter = loader.getController();

            // Pass the parameters to the Simulation class
            Simulation simulation = new Simulation(
                    mapHeight, mapWidth, startingGrassCount, energyFromEatingPlants, numberOfPlantsGrownDaily,
                    numAnimals, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations,
                    numberOfGenes, startingEnergy, AnimalTypes.Animal, MapType.EarthMap); //TODO REPLACE THIS WITH CRAZY ANIMAL OPTION

            SimulationEngine engine = new SimulationEngine(List.of(simulation));

            newPresenter.moveDescriptionLabel.setText("Simulation started!");

            new Thread(engine::runSync).start();

            Scene scene = new Scene(newRoot);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
