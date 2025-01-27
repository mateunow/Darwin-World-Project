package darwinProject.presenter;

import darwinProject.enums.AnimalType;
import darwinProject.enums.MapType;

public class SimulationData {
    public int mapWidth, mapHeight, numAnimals, startingGrassCount, energyFromEatingPlants;
    public int numberOfPlantsGrownDaily, startingEnergy, energyReadyToReproduce, energyToReproduce;
    public int minNumberOfMutations, maxNumberOfMutations, numberOfGenes;
    public AnimalType selectedAnimalType;
    public MapType selectedMapType;

    public SimulationData(int mapWidth, int mapHeight, int numAnimals, int startingGrassCount,
                          int energyFromEatingPlants, int numberOfPlantsGrownDaily, int startingEnergy,
                          int energyReadyToReproduce, int energyToReproduce, int minNumberOfMutations,
                          int maxNumberOfMutations, int numberOfGenes, AnimalType selectedAnimalType,
                          MapType selectedMapType) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.numAnimals = numAnimals;
        this.startingGrassCount = startingGrassCount;
        this.energyFromEatingPlants = energyFromEatingPlants;
        this.numberOfPlantsGrownDaily = numberOfPlantsGrownDaily;
        this.startingEnergy = startingEnergy;
        this.energyReadyToReproduce = energyReadyToReproduce;
        this.energyToReproduce = energyToReproduce;
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
        this.numberOfGenes = numberOfGenes;
        this.selectedAnimalType = selectedAnimalType;
        this.selectedMapType = selectedMapType;
    }
}
