package darwinProject;

import darwinProject.presenter.SimulationApp;
import darwinProject.presenter.SimulationSettingsApp;
import javafx.application.Application;

public class WorldGUI {
  //Apikacja niestety nie działa


     public static void main(String[] args) {
         try {
        Application.launch(SimulationSettingsApp.class, args);  // Uruchomienie aplikacji z oknem formularza


        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 }
