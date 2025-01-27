package darwinProject.statistics;

import darwinProject.model.Animal;
import darwinProject.model.Grass;
import darwinProject.model.maps.AbstractWorldMap;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class SimulationStatistics {
    private final AbstractWorldMap map;
    private int totalEnergy;
    private int totalAnimals;
    private int totalGrass;
    private int totalChildren;
    private final List<Integer> animalEnergies = new ArrayList<>();
    private final List<Integer> plantConsumptions = new ArrayList<>();

    // History lists for keeping track of previous data points
    private final List<Double> energyHistory = new ArrayList<>();
    private final List<Double> animalHistory = new ArrayList<>();
    private final List<Double> grassHistory = new ArrayList<>();

    private VBox root;
    private Text numberOfAnimalsText;
    private Text totalGrassText;
    private Text totalEnergyText;
    private Text averageEnergyText;
    private Text averagePlantConsumptionsText;
    private Text averageChildrenText;

    // Chart variables
    private LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> energySeries;
    private XYChart.Series<Number, Number> animalSeries;
    private XYChart.Series<Number, Number> grassSeries;

    public SimulationStatistics(AbstractWorldMap map) {
        this.map = map;
        // Initialize the texts
        numberOfAnimalsText = new Text();
        totalGrassText = new Text();
        totalEnergyText = new Text();
        averageEnergyText = new Text();
        averagePlantConsumptionsText = new Text();
        averageChildrenText = new Text();

        // Initialize chart data series
        energySeries = new XYChart.Series<>();
        animalSeries = new XYChart.Series<>();
        grassSeries = new XYChart.Series<>();

        // Add series to chart
        energySeries.setName("Energy");
        animalSeries.setName("Animals");
        grassSeries.setName("Grass");
    }

    public void updateStatistics() {
        totalEnergy = 0;
        totalAnimals = 0;
        totalGrass = 0;
        totalChildren = 0;
        animalEnergies.clear();
        plantConsumptions.clear();

        for (var element : map.getElements()) {
            if (element instanceof Animal animal) {
                totalAnimals++;
                totalEnergy += animal.getEnergy();
                totalChildren += animal.getChildrenCount();
                animalEnergies.add(animal.getEnergy());
                plantConsumptions.add(animal.getPlantsEaten());
            } else if (element instanceof Grass) {
                totalGrass++;
            }
        }

        // Update the text elements with new statistics
        numberOfAnimalsText.setText("Number of Animals: " + totalAnimals);
        totalGrassText.setText("Total Grass: " + totalGrass);
        totalEnergyText.setText("Total Energy: " + totalEnergy);
        averageEnergyText.setText("Average Energy: " + getAverageEnergy());
        averagePlantConsumptionsText.setText("Average Plants Consumed: " + getAveragePlantConsumptions());
        averageChildrenText.setText("Average Children per Animal: " + getAverageChildrenPerAnimal());

        // Store historical data for the chart
        energyHistory.add(getAverageEnergy());
        animalHistory.add((double) totalAnimals);
        grassHistory.add((double) totalGrass);

        // Add new data points to the chart
        energySeries.getData().add(new XYChart.Data<>(energyHistory.size(), getAverageEnergy()));
        animalSeries.getData().add(new XYChart.Data<>(animalHistory.size(), totalAnimals));
        grassSeries.getData().add(new XYChart.Data<>(grassHistory.size(), totalGrass));
    }

    public double getAverageEnergy() {
        return totalAnimals > 0 ? (double) totalEnergy / totalAnimals : 0;
    }

    public double getAveragePlantConsumptions() {
        return totalAnimals > 0 ? plantConsumptions.stream().mapToInt(Integer::intValue).average().orElse(0) : 0;
    }

    public double getAverageChildrenPerAnimal() {
        return totalAnimals > 0 ? (double) totalChildren / totalAnimals : 0;
    }

    public Map<Integer, Long> getMostCommonGenomes() {
        Map<Integer, Long> genomeCounts = new HashMap<>();
        for (var element : map.getElements()) {
            if (element instanceof Animal animal) {
                for (Integer gene : animal.getGenome()) {
                    genomeCounts.put(gene, genomeCounts.getOrDefault(gene, 0L) + 1);
                }
            }
        }
        return genomeCounts;
    }

    public void showStatisticsWindow() {
        Stage stage = new Stage();
        stage.setTitle("Simulation Statistics");

        // Create Number Axes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Steps");
        xAxis.setTickUnit(1);  // Set the axis step to 1 for each update
        xAxis.setAutoRanging(false); // Set to manual so it doesn't zoom automatically
        xAxis.setLowerBound(0);  // Start at step 0
        xAxis.setUpperBound(100); // Update upper bound as required
        xAxis.setTickLabelFormatter(new javafx.scene.chart.NumberAxis.DefaultFormatter(xAxis, "", ""));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        // Create a line chart using x and y axes
        chart = new LineChart<>(xAxis, yAxis);
        chart.getData().add(energySeries);
        chart.getData().add(animalSeries);
        chart.getData().add(grassSeries);

        // Customize appearance of the chart
        chart.setTitle("Simulation Over Time");
        chart.setCreateSymbols(false); // Disable line symbols (points)

        // Customizing the series lines (colors, width, etc.)
        energySeries.getNode().setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2;");
        animalSeries.getNode().setStyle("-fx-stroke: #0000FF; -fx-stroke-width: 2;");
        grassSeries.getNode().setStyle("-fx-stroke: #008000; -fx-stroke-width: 2;");

        // Set background color (optional)
        chart.setStyle("-fx-background-color: #F0F0F0;");

        // Initialize VBox and add the Text elements and the chart
        root = new VBox(10,
                numberOfAnimalsText,
                totalGrassText,
                totalEnergyText,
                averageEnergyText,
                averagePlantConsumptionsText,
                averageChildrenText,
                chart
        );

        // Show initial statistics and chart
        updateStatistics();

        // Set up the stage with the VBox
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void refreshStatistics() {
        updateStatistics(); // Update the data
        // No need to manually refresh the statistics since the chart is bound to the updated data
    }
}
