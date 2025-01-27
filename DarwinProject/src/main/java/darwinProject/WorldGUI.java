package darwinProject;

import darwinProject.presenter.SimulationApp;
import darwinProject.presenter.SimulationSettingsApp;
import javafx.application.Application;

public class WorldGUI {
    public static void main(String[] args) {
        try {
            System.out.println("Hello World!");
            Application.launch(SimulationApp.class, args);


        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


//    public static void main(String[] args) {
//        Application.launch(SimulationSettingsApp.class, args);  // Uruchomienie aplikacji z oknem formularza
//    }
//}
