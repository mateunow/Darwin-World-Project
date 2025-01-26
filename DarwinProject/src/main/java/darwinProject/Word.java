package darwinProject;

public class Word {
         public static void main (String[] args){
                Simulation simulation = new Simulation(10, 10, 20, 50, 5, 15, 50, 20, 0, 3, 7, 150);
                simulation.run();
             System.out.println("System zakończył działanie");
         }
}