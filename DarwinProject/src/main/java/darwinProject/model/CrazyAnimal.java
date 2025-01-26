package darwinProject.model;

public class CrazyAnimal extends Animal {
    //TODO tego private finala usunąć może jakoś do animala

    public CrazyAnimal(Vector2d position, Integer numberOfGenes, Integer startingEnergy) {
        super(position, numberOfGenes, startingEnergy, 1, 1, 1, 1);
    }

    @Override
    public void turn(Integer turnCount){
        this.direction = this.direction.turn(turnCount);
        int randChoice = rand.nextInt(100);

        if (randChoice < 80) {
            currentGene++;
        } else {
            currentGene = rand.nextInt(numberOfGenes);
        }
        currentGene %= maxGene;
    }
}

