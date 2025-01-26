package darwinProject.statistics;

import darwinProject.model.Animal;

public class AnimalStatistics {
    private final Animal animal;

    public AnimalStatistics(Animal animal) {
        this.animal = animal;
    }

    public String getGenome() {
        return animal.getGenome().toString();
    }

    public int getActivePart() {
        return animal.getGenome().get(animal.currentGene);
    }

    public int getEnergy() {
        return animal.getEnergy();
    }

    public int getEatenPlants() {
        return animal.getPlantsEaten();
    }

    public int getChildrenCount() {
        return animal.getChildrenCount();
    }

    public int getOffspringCount() {
        return animal.getChildrenCount();
    }

    public int getAge() {
        return animal.getDaysLived();
    }

    public String getDeathDay() {
        return animal.getDaysLived() > 0 ? String.valueOf(animal.getDaysLived()) : "Alive";
    }
}
