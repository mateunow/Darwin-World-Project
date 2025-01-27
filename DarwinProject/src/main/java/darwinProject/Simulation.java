package darwinProject;

import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.Animal;
import darwinProject.model.Vector2d;
import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.maps.EarthMap;
import darwinProject.presenter.SimulationPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final List<Vector2d> animalsPositions;
    private final AbstractWorldMap world;
    private Random random = new Random();
    private final Integer initialNumberOfAnimals;
    private SimulationPresenter presenter;  // Odwołanie do GUI, by móc aktualizować mapę

    public Simulation(Integer mapHeight, Integer mapWidth, Integer startingGrassCount,
                      Integer energyFromEatingPlants, Integer numberOfPlantsGrownDaily, Integer initialNumberOfAnimals,
                      Integer energyReadyToReproduce, Integer energyToReproduce, Integer minNumberOfMutations,
                      Integer maxNumberOfMutations, Integer numberOfGenes, Integer startingEnergy, SimulationPresenter presenter) {
        // Inicjalizacja mapy, zwierząt itp.
        this.world = new EarthMap(mapHeight, mapWidth, startingGrassCount, numberOfPlantsGrownDaily, energyFromEatingPlants);
        this.animals = new ArrayList<>();
        this.animalsPositions = new ArrayList<>();
        this.initialNumberOfAnimals = initialNumberOfAnimals;
        this.presenter = presenter;  // Ustawiamy GUI, by później odświeżać mapę

        // Inicjalizowanie zwierząt
        for (int i = 0; i < initialNumberOfAnimals; i++) {
            try {
                Vector2d position = new Vector2d(random.nextInt(mapWidth), random.nextInt(mapHeight));
                Animal animal = new Animal(position, numberOfGenes, startingEnergy, energyReadyToReproduce,
                        energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
                world.place(animal);
                this.animals.add(animal);
                this.animalsPositions.add(position);
            } catch (IncorrectPositionException e) {
                System.out.println("Incorrect position " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        int animalsCount = initialNumberOfAnimals;
        boolean running = true;

        while (running) {
//            System.out.println(animalsCount + " animals");
            for (int i = 0; i < animalsCount; i++) {
                // Przemieszczanie zwierząt
                world.move(animals.get(i));
            }

            // Przykładowo dodanie jedzenia (jeśli masz funkcję dla jedzenia):
            // TODO: Dodaj funkcje do wyżywienia zwierząt
            for (Vector2d position : animalsPositions) {
                // TODO: Logika konsumowania trawy przez zwierzęta
            }

            // Generowanie nowej trawy
            world.generateNewGrassPositions();

            // Odświeżenie mapy na GUI co sekundę
            presenter.updateMap();

            try {
                // Pauza, by proces działał co sekundę
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Sprawdzamy warunki końcowe symulacji, np. liczba żywych zwierząt
            if (animals.isEmpty()) {
                running = false;  // Kończymy symulację, jeśli nie ma zwierząt
            }
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }

    public void kill(Animal animal) {
        animals.remove(animal); // Usuwamy zwierzę z listy
    }

    public AbstractWorldMap getWorld() {
        return world;
    }
}
