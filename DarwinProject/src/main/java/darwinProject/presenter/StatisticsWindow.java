package darwinProject.presenter;

import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.maps.EarthMap;
import darwinProject.model.WorldElement;
import darwinProject.model.Animal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsWindow extends Application {
    private TextArea statisticsArea;

    private final AbstractWorldMap map; // Twoja mapa (np. EarthMap)

    // Konstruktor przyjmujący mapę
    public StatisticsWindow(AbstractWorldMap map) {
        this.map = map;
    }

    public static void launchWindow(AbstractWorldMap map) {
        // Użycie launch() wymaga statycznego startu, dlatego przesyłamy mapę jako singleton
        StatisticsWindow window = new StatisticsWindow(map);
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Statystyki Symulacji");

        // Tworzymy TextArea do wyświetlania statystyk
        statisticsArea = new TextArea();
        statisticsArea.setEditable(false);
        statisticsArea.setWrapText(true);

        Button refreshButton = new Button("Odśwież statystyki");
        refreshButton.setOnAction(event -> updateStatistics());

        VBox layout = new VBox(10, statisticsArea, refreshButton);
        layout.setPadding(new javafx.geometry.Insets(10));

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicjalne załadowanie statystyk
        updateStatistics();
    }

    /**
     * Metoda do odświeżania statystyk.
     */
    private void updateStatistics() {
        StringBuilder statsBuilder = new StringBuilder();

        // Liczba zwierząt na mapie
        long animalCount = map.getElements().stream()
                .filter(element -> element instanceof Animal)
                .count();

        // Liczba roślin (Grass) na mapie
        long grassCount = map.getElements().stream()
                .filter(element -> !(element instanceof Animal))
                .count();

        // Najczęściej występujący kierunek zwierząt
        Map<String, Long> directions = map.getElements().stream()
                .filter(element -> element instanceof Animal)
                .map(element -> ((Animal) element).getDirection().toString())
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        String mostCommonDirection = directions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Brak danych");

        statsBuilder.append("Liczba zwierząt: ").append(animalCount).append("\n");
        statsBuilder.append("Liczba roślin: ").append(grassCount).append("\n");
        statsBuilder.append("Najczęstszy kierunek zwierząt: ").append(mostCommonDirection).append("\n");

        // Wstawianie do TextArea
        statisticsArea.setText(statsBuilder.toString());
    }
}
