package darwinProject;

import darwinProject.enums.AnimalType;
import darwinProject.enums.MapType;
import darwinProject.model.CrazyAnimal;
import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.Animal;
import darwinProject.model.Vector2d;
import darwinProject.exceptions.IncorrectPositionException;
import darwinProject.model.maps.EarthMap;
import darwinProject.model.maps.WaterMap;

import java.util.Random;

public class Simulation implements Runnable
{
    private final AbstractWorldMap world;


    public Simulation(Integer mapHeight, Integer mapWidth, Integer startingGrassCount,
                      Integer energyFromEatingPlants, Integer numberOfPlantsGrownDaily, Integer initialNumberOfAnimals,
                      Integer energyReadyToReproduce, Integer energyToReproduce, Integer minNumberOfMutations, Integer maxNumberOfMutations, Integer numberOfGenes, Integer startingEnergy, AnimalType type, MapType mapType) {

        //TODO ADD MAP TYPE
        if (mapType == MapType.EarthMap) {
            this.world = new EarthMap(mapHeight, mapWidth, startingGrassCount, numberOfPlantsGrownDaily, energyFromEatingPlants);
        }
        else {
            this.world = new WaterMap(mapHeight, mapWidth, startingGrassCount, numberOfPlantsGrownDaily, energyFromEatingPlants);
        }
        Random random = new Random();

        for (int i=0; i<initialNumberOfAnimals; i++) {
            try {

                Vector2d position = new Vector2d(random.nextInt(mapWidth), random.nextInt(mapHeight));
                if (type == AnimalType.CrazyAnimal) {
                    CrazyAnimal animal = new CrazyAnimal(position, numberOfGenes, startingEnergy, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
                    world.place(animal);
                } else {
                    Animal animal = new Animal(position, numberOfGenes, startingEnergy, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
                    world.place(animal);
                }
            }
            catch (IncorrectPositionException e) {
                System.out.println("Incorrect position " + e.getMessage());
            }
        }

    }

    public void run(){

        boolean running = true;
        int i = 0;
        while (running) {
            System.out.println("day " + i);
            i++;
//TODO dodaj warunek zakończenia pętli
//
            world.handleMovement();
            world.eatPlants();
            world.handlePlantConsumption();
            world.handleReproduction();
            world.generateNewGrassPositions();
//            System.out.println(world);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


        }
    }

}

