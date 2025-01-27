package darwinProject.presenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SimulationSettingsApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulationSettings.fxml"));
        AnchorPane root = loader.load();
        primaryStage.setTitle("Ustawienia Symulacji");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        System.out.println("wstawiono dane darwinProject");
    }
}
