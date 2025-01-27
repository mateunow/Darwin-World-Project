package darwinProject.presenter;

import darwinProject.Simulation;
import darwinProject.enums.AnimalType;
import darwinProject.enums.MapType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;


import javafx.scene.control.ComboBox;

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

    @FXML
    private ComboBox<AnimalType> animalTypeComboBox;  // Updated ComboBox for animal type
    @FXML
    private ComboBox<MapType> mapTypeComboBox;  // ComboBox for map type

    // Method to initialize the form
    @FXML
    public void initialize() {
        errorMessageLabel.setText(""); // Hide errors initially

        // Initialize ComboBoxes with the enum values
        animalTypeComboBox.getItems().setAll(AnimalType.values());  // Updated enum
        mapTypeComboBox.getItems().setAll(MapType.values());
    }

    public SimulationData getSimulationData() {
        try {
            // Get values from all the fields
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int numAnimals = Integer.parseInt(numAnimalsField.getText());
            int startingGrassCount = Integer.parseInt(startingGrassCountField.getText());
            int energyFromEatingPlants = Integer.parseInt(energyFromEatingPlantsField.getText());
            int numberOfPlantsGrownDaily = Integer.parseInt(numberOfPlantsGrownDailyField.getText());
            int startingEnergy = Integer.parseInt(startingEnergyField.getText());
            int energyReadyToReproduce = Integer.parseInt(energyReadyToReproduceField.getText());
            int energyToReproduce = Integer.parseInt(energyToReproduceField.getText());
            int minNumberOfMutations = Integer.parseInt(minNumberOfMutationsField.getText());
            int maxNumberOfMutations = Integer.parseInt(maxNumberOfMutationsField.getText());
            int numberOfGenes = Integer.parseInt(numberOfGenesField.getText());

            // Get selected values for animal and map types
            AnimalType selectedAnimalType = animalTypeComboBox.getValue();
            MapType selectedMapType = mapTypeComboBox.getValue();

            // Return a new SimulationData object
            return new SimulationData(
                    mapWidth, mapHeight, numAnimals, startingGrassCount, energyFromEatingPlants,
                    numberOfPlantsGrownDaily, startingEnergy, energyReadyToReproduce, energyToReproduce,
                    minNumberOfMutations, maxNumberOfMutations, numberOfGenes, selectedAnimalType, selectedMapType
            );
        } catch (NumberFormatException e) {
            // Handle invalid input (e.g., show error message)
            return null; // You could show a dialog here to indicate failure
        }
    }
    // Method to start the simulation based on user input
    @FXML
    private void onStartSimulation() {
        try {
            // Get values from all the fields
            int mapWidth = Integer.parseInt(mapWidthField.getText());
            int mapHeight = Integer.parseInt(mapHeightField.getText());
            int numAnimals = Integer.parseInt(numAnimalsField.getText());

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

            // Get the selected animal and map types
            AnimalType selectedAnimalType = animalTypeComboBox.getValue();  // Updated enum reference
            MapType selectedMapType = mapTypeComboBox.getValue();

            // Check that values are within valid ranges
            if (mapWidth < 1 || mapWidth > 10001 || mapHeight < 1 || mapHeight > 10001 ||
                    numAnimals < 1 || numAnimals > 10001 ||
                    startingGrassCount < 1 || startingGrassCount > 10001 || energyFromEatingPlants < 1 || energyFromEatingPlants > 10001 ||
                    numberOfPlantsGrownDaily < 1 || numberOfPlantsGrownDaily > 10001 || startingEnergy < 1 || startingEnergy > 10001 ||
                    energyReadyToReproduce < 1 || energyReadyToReproduce > 10001 || energyToReproduce < 1 || energyToReproduce > 10001 ||
                    minNumberOfMutations < 0 || minNumberOfMutations > 10001 || maxNumberOfMutations < 0 || maxNumberOfMutations > 10001 ||
                    numberOfGenes < 1 || numberOfGenes > 10001) {
                errorMessageLabel.setText("Błąd: Wszystkie dane muszą być liczbami od 1 do 10001!");
                return;
            }

            // Start a new simulation window with selected animal and map types
            startNewSimulationWindow(mapWidth, mapHeight, numAnimals, startingGrassCount, energyFromEatingPlants,
                    numberOfPlantsGrownDaily, startingEnergy, energyReadyToReproduce, energyToReproduce, minNumberOfMutations,
                    maxNumberOfMutations, numberOfGenes, selectedAnimalType, selectedMapType);

        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Błąd: Proszę wprowadzić poprawne liczby!");
        }
    }

    // Create a new simulation window with the given parameters
    private void startNewSimulationWindow(int mapWidth, int mapHeight, int numAnimals,
                                          int startingGrassCount, int energyFromEatingPlants, int numberOfPlantsGrownDaily,
                                          int startingEnergy, int energyReadyToReproduce, int energyToReproduce,
                                          int minNumberOfMutations, int maxNumberOfMutations, int numberOfGenes,
                                          AnimalType animalType, MapType mapType) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Simulation Window");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
            BorderPane newRoot = loader.load();

            // Get the controller to pass simulation parameters
            SimulationPresenter presenter = loader.getController();


            // Create and configure the simulation
            Simulation simulation = new Simulation(
                    mapHeight, mapWidth, startingGrassCount, energyFromEatingPlants, numberOfPlantsGrownDaily,
                    numAnimals, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations,
                    numberOfGenes, startingEnergy, animalType, mapType);

            // Assign the world map and simulation to the presenter
            presenter.setWorldMap(simulation.getWorldMap());
            presenter.simulation = simulation;

            // Initialize the simulation engine
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            presenter.engine = engine;

            // Start the simulation engine asynchronously
            new Thread(() -> engine.runAsync()).start();

            // Show the simulation UI
            Scene scene = new Scene(newRoot);
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
