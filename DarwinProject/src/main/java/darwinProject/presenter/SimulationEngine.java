package darwinProject.presenter;


import darwinProject.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine{
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);


    public SimulationEngine(List<Simulation> simulations)
    {
        this.simulations = simulations;
    }

    public void runSync(){
        for (Simulation simulation : simulations) {
            simulation.run();
        }
    }
    public void runAsync(){
        for (Simulation simulation : simulations) {
            Thread simulationRun = new Thread(simulation);
            threads.add(simulationRun);
            simulationRun.start();
        }
        awaitSimulationEnd();
    }


    public void runAsyncInThreadPool(){
        for (Simulation simulation : simulations) {
            executor.submit(simulation);
        }
        awaitSimulationEnd();
    }

    private void awaitSimulationEnd() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)){
                executor.shutdownNow();
            }
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    }

