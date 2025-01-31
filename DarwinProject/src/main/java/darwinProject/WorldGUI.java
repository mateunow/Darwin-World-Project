package darwinProject; // pakiet powinien mieć postać odwrotnej nazwy domenowej

import darwinProject.presenter.SimulationApp;
import darwinProject.presenter.SimulationSettingsApp;
import javafx.application.Application;

public class WorldGUI {
    //Apikacja niestety nie działa
    public static void main(String[] args) {
        try {
            System.out.println("Hello World!");
            Application.launch(SimulationApp.class, args);


        } catch (IllegalArgumentException e) { // czy to się może zdarzyć?
            System.out.println("Error: " + e.getMessage());
        }
    }
}


//     public static void main(String[] args) {
//         Application.launch(SimulationSettingsApp.class, args);  // Uruchomienie aplikacji z oknem formularza
//     }
// }
