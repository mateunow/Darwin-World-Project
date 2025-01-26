package darwinProject;

import darwinProject.presenter.SimulationApp;
import javafx.application.Application;

public class WorldGUI {
    public static void main(String[] args) {
        try {
            Application.launch(SimulationApp.class, args);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
