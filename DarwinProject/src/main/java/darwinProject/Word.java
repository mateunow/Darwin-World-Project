package darwinProject;


import darwinProject.model.Vector2d;
import darwinProject.model.maps.EarthMap;
import darwinProject.presenter.SimulationApp;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class Word {
         public static void main (String[] args){
//             Application.launch(SimulationApp.class, args);
             EarthMap map = new EarthMap(10, 10, 10, 2, 20);
             List<Vector2d> positions = List.of(new Vector2d(1,1), new Vector2d(2,2));
                Simulation simulation = new Simulation(100, 100, 20, 20, 5, 5, 50, 20, 0, 3, 7, 50);
                simulation.run();
             System.out.println("System zakończył działanie");
         }
}