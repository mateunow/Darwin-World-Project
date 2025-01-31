package darwinProject.model;

import java.util.ArrayList;

public class CrazyAnimal extends Animal {
    public CrazyAnimal(Vector2d position, Integer numberOfGenes, Integer startingEnergy, Integer energyReadyToReproduce, Integer energyToReproduce, Integer minNumberOfMutations, Integer maxNumberOfMutations) {
        super(position, numberOfGenes, startingEnergy, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
    }

    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome, Integer energy, Animal firstParent, Animal secondParent, Integer energyReadyToReproduce, Integer energyToReproduce, Integer minNumberOfMutations, Integer maxNumberOfMutations) {
        super(position, genome, energy, firstParent, secondParent, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
    }

    @Override
    public void turn(Integer turnCount) {
        this.direction = this.direction.turn(turnCount);
        int randChoice = rand.nextInt(100);

        if (randChoice < 80) {
            currentGene++;
        } else {
            currentGene = rand.nextInt(numberOfGenes);
        }
        currentGene %= maxGene;
    }

    @Override
    public Animal reproduceWithOtherAnimal(Animal animal) {
        ArrayList<Integer> childGenome = createChildGenome(this, animal);
        this.childrenCount++;
        this.energy -= energyToReproduce;
        animal.childrenCount++;
        animal.energy -= energyToReproduce;
        return new CrazyAnimal(this.getPosition(), childGenome, 2 * energyToReproduce, this, animal, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
    }

}

