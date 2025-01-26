package darwinProject.statistics;

import darwinProject.model.Animal;
import darwinProject.model.maps.AbstractWorldMap;
import darwinProject.model.Grass;

import java.util.*;

public class SimulationStatistics {
    private final AbstractWorldMap map;
    private int totalEnergy;
    private int totalAnimals;
    private int totalGrass;
    private int totalChildren;
    private final List<Integer> animalEnergies;
    private final List<Integer> plantConsumptions;

    public SimulationStatistics(AbstractWorldMap map) {
        this.map = map;
        this.totalEnergy = 0;
        this.totalAnimals = 0;
        this.totalGrass = 0;
        this.totalChildren = 0;
        this.animalEnergies = new ArrayList<>();
        this.plantConsumptions = new ArrayList<>();
    }

    public void updateStatistics() {
        totalEnergy = 0;
        totalAnimals = 0;
        totalGrass = 0;
        totalChildren = 0;
        animalEnergies.clear();
        plantConsumptions.clear();

        for (var element : map.getElements()) {
            if (element instanceof Animal animal) {
                totalAnimals++;
                totalEnergy += animal.getEnergy();
                totalChildren += animal.getChildrenCount();
                animalEnergies.add(animal.getEnergy());
                plantConsumptions.add(animal.getPlantsEaten());
            } else if (element instanceof Grass) {
                totalGrass++;
            }
        }
    }

    public int getTotalEnergy() {
        return totalEnergy;
    }

    public int getTotalAnimals() {
        return totalAnimals;
    }

    public int getTotalGrass() {
        return totalGrass;
    }

    public int getTotalChildren() {
        return totalChildren;
    }

    public double getAverageEnergy() {
        return totalAnimals > 0 ? (double) totalEnergy / totalAnimals : 0;
    }

    public double getAveragePlantConsumptions() {
        return totalAnimals > 0 ? plantConsumptions.stream().mapToInt(Integer::intValue).average().orElse(0) : 0;
    }

    public double getAverageChildrenPerAnimal() {
        return totalAnimals > 0 ? (double) totalChildren / totalAnimals : 0;
    }

    public Map<Integer, Long> getMostCommonGenomes() {
        Map<Integer, Long> genomeCounts = new HashMap<>();

        for (var element : map.getElements()) {
            if (element instanceof Animal animal) {
                for (Integer gene : animal.getGenome()) {
                    genomeCounts.put(gene, genomeCounts.getOrDefault(gene, 0L) + 1);
                }
            }
        }
        return genomeCounts;
    }

    public void printStatistics() {
        System.out.println("=== Simulation Statistics ===");
        System.out.println("Total Animals: " + totalAnimals);
        System.out.println("Total Grass: " + totalGrass);
        System.out.println("Total Energy: " + totalEnergy);
        System.out.println("Average Energy: " + getAverageEnergy());
        System.out.println("Average Plants Consumed per Animal: " + getAveragePlantConsumptions());
        System.out.println("Average Children per Animal: " + getAverageChildrenPerAnimal());
        System.out.println("Most Common Genomes: " + getMostCommonGenomes());
    }
}
