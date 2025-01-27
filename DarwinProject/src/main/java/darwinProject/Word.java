package darwinProject;

import darwinProject.enums.AnimalType;
import darwinProject.enums.MapType;

public class Word {
         public static void main (String[] args){
                Simulation simulation = new Simulation(10, 10, 20, 50, 9, 10, 50, 20, 0, 3, 7, 150, AnimalType.Animal, MapType.EarthMap);
                simulation.run();
             System.out.println("System zakończył działanie");
         }
}