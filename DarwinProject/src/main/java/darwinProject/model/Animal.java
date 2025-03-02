package darwinProject.model;

import darwinProject.enums.MapDirection;
import darwinProject.model.maps.WorldMap;
import darwinProject.model.util.Boundary;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Animal implements Comparable<Animal>, WorldElement {
    protected MapDirection direction;
    private Vector2d position;
    protected int energy;
    protected final int maxGene = 7;
    private final ArrayList<Integer> genome;
    public int currentGene;
    private int daysLived = 0;
    private int plantsEaten;
    private int dayOfDeath;
    private final Animal firstParent;
    private final Animal secondParent;
    private int offspringCount = 0;
    protected int childrenCount = 0;
    protected final Random rand = new Random();
    protected final Integer energyReadyToReproduce;
    protected final Integer energyToReproduce;
    protected final Integer minNumberOfMutations;
    protected final Integer maxNumberOfMutations;
    protected final int numberOfGenes;


    public Animal(Vector2d position, Integer numberOfGenes, Integer startingEnergy, Integer energyReadyToReproduce, Integer energyToReproduce, Integer minNumberOfMutations, Integer maxNumberOfMutations){
        //initial animal
        this.position = position;
        this.energy = startingEnergy;
        this.genome = new ArrayList<>();
        for (int i = 0; i < numberOfGenes; i++) {
            genome.add(rand.nextInt(maxGene + 1));
        }
        this.currentGene = rand.nextInt(numberOfGenes);
        this.numberOfGenes = numberOfGenes;
        this.direction = MapDirection.values()[rand.nextInt(MapDirection.values().length)];
        this.firstParent = null;
        this.secondParent = null;
        this.energyReadyToReproduce = energyReadyToReproduce;
        this.energyToReproduce = energyToReproduce;
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
    }

    public Animal(Vector2d position, ArrayList<Integer> genome,  Integer energy, Animal firstParent, Animal secondParent, Integer energyReadyToReproduce, Integer energyToReproduce, Integer minNumberOfMutations, Integer maxNumberOfMutations){
        //child animal born after the simulation started
        this.position = position;
        this.energy = energy;
        this.genome = genome;
        this.numberOfGenes = genome.size();
        this.currentGene = rand.nextInt(genome.size());
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.direction = MapDirection.values()[rand.nextInt(MapDirection.values().length)];
        this.energyReadyToReproduce = energyReadyToReproduce;
        this.energyToReproduce = energyToReproduce;
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
    }

    @Override
    public int compareTo(Animal other) {
        if (this.energy != other.energy) {
            return Integer.compare(other.energy, this.energy);
        }

        if (this.daysLived != other.daysLived) {
            return Integer.compare(other.daysLived, this.daysLived);
        }
        if (this.childrenCount != other.childrenCount) {
            return Integer.compare(other.childrenCount, this.childrenCount);}
        if(rand.nextInt(2) == 0 ) {
            return -1;
        }
        else {
            return 1;
        }
    }


//Kod z zastosowaniem pod aplikację
//    public String toString() {
//        return switch (this.direction) {
//            case NORTH -> "↑";        // Zwróć strzałkę w górę (Unicode: \u2191)
//            case NORTHEAST -> "↗";    // Zwróć strzałkę w górny-prawy róg (Unicode: \u2197)
//            case EAST -> "→";         // Zwróć strzałkę w prawo (Unicode: \u2192)
//            case SOUTHEAST -> "↘";    // Zwróć strzałkę w dolny-prawy róg (Unicode: \u2198)
//            case SOUTH -> "↓";        // Zwróć strzałkę w dół (Unicode: \u2193)
//            case SOUTHWEST -> "↙";    // Zwróć strzałkę w dolny-lewy róg (Unicode: \u2199)
//            case WEST -> "←";         // Zwróć strzałkę w lewo (Unicode: \u2190)
//            case NORTHWEST -> "↖";    // Zwróć strzałkę w górny-lewy róg (Unicode: \u2196)
//        };
//    }

    public String toString() {

        return switch (this.direction) {
            case NORTH -> "^";
            case NORTHEAST -> "NE";
            case EAST -> ">";
            case SOUTHEAST -> "SE";
            case SOUTH -> "v";
            case SOUTHWEST -> "SW";
            case WEST -> "<";
            case NORTHWEST -> "NW";

        };

//        Color color = getColorBasedOnEnergy(); // Pobieranie koloru na podstawie energii

        // Jeśli chcesz, możesz po prostu dołączyć kolory do znaków, ale ostateczne rysowanie musi się odbywać w JavaFX
//        return directionSymbol;
    }




    public Color getColorBasedOnEnergy() {
        if (this.energy < 20) {
            return Color.RED;      // Niska energia - czerwony
        } else if (this.energy < 50) {
            return Color.ORANGE;   // Średnia energia - pomarańczowy
        } else {
            return Color.GREEN;    // Wysoka energia - zielony
        }
    }


    public void move(WorldMap map) {
        reduceEnergy(20);
        Vector2d potentialNewPosition = this.position.add(this.getDirection().toUnitVector());

        Boundary boundary = map.getCurrentBounds();
        int yPosition = potentialNewPosition.getY();
        int mapWidth = boundary.upperRight().getX();
        int potentialX = potentialNewPosition.getX();
        if ( (yPosition == -1) || (yPosition > boundary.upperRight().getY())) {
            this.turn(4);
        }
        else {
            if (potentialX > mapWidth) {
                this.position = new Vector2d(0, yPosition);
                this.turn(genome.get(currentGene));
            }
            else if(potentialX < 0) {
                this.position = new Vector2d(mapWidth, yPosition);
                this.turn(genome.get(currentGene));
            }
            else {
                this.position = potentialNewPosition;
                this.turn(genome.get(currentGene));
            }
        }
        if (isDead()) {
            this.die();
        }
        else {
            daysLived += 1;
        }
    }

    public Animal reproduceWithOtherAnimal(Animal animal) {
        ArrayList<Integer> childGenome = createChildGenome(this, animal);
        this.childrenCount++;
        this.energy -= energyToReproduce;
        animal.childrenCount++;
        animal.energy -= energyToReproduce;
        return new Animal(this.getPosition(), childGenome, 2 * energyToReproduce, this, animal, energyReadyToReproduce, energyToReproduce, minNumberOfMutations, maxNumberOfMutations);
    }

    public ArrayList<Integer> createChildGenome(Animal animal1, Animal animal2) {
        int sumOfEnergy = animal1.energy + animal2.energy;


        Animal strongerParent = animal1.energy > animal2.energy ? animal1 : animal2;
        Animal weakerParent = animal2.energy > animal1.energy ? animal2 : animal1;

        double strongerParentEnergyRatio = (double) strongerParent.energy / sumOfEnergy;

        int genesFromStrongerParent = (int) (strongerParentEnergyRatio * strongerParent.genome.size());
        int genesFromWeakerParent = strongerParent.genome.size() - genesFromStrongerParent;

        Random rand = new Random();
        int randomSide = rand.nextInt(2);

        ArrayList<Integer> newGenome = new ArrayList<>();

        if (randomSide == 0) {
            // Inherit the left side from the stronger parent and the right side from the weaker parent
            newGenome.addAll(strongerParent.genome.subList(0, genesFromStrongerParent));
            newGenome.addAll(weakerParent.genome.subList(genesFromStrongerParent, weakerParent.genome.size()));
        } else {
            // Inherit the right side from the stronger parent and the left side from the weaker parent
            newGenome.addAll(weakerParent.genome.subList(0, genesFromWeakerParent));
            newGenome.addAll(strongerParent.genome.subList(genesFromWeakerParent, strongerParent.genome.size()));
        }

        // Apply mutations to some genes in the new genome
        int genomeSize = newGenome.size();
        int numMutations = rand.nextInt(maxNumberOfMutations-minNumberOfMutations) + minNumberOfMutations;
        for (int i = 0; i < numMutations; i++) {
            int geneIndex = rand.nextInt(genomeSize);
            int newGeneValue = rand.nextInt(maxGene + 1);
            newGenome.set(geneIndex, newGeneValue);
        }

        return newGenome;
    }


    public void turn(Integer turnCount){
        this.direction = this.direction.turn(turnCount);
        currentGene++;
        currentGene%=maxGene;
    }

    public void addEnergy(int energy){
        this.energy += energy;
        plantsEaten += 1;
    }
    public void reduceEnergy(int energy){
        this.energy -= energy;
    }
    public Vector2d getPosition(){
        return this.position;
    }
    public MapDirection getDirection() {
        return this.direction;
    }

    public void die() {
        this.dayOfDeath = this.daysLived;
    }
    public boolean isDead() {
        return (this.energy <= 0);
    }
    public boolean canReproduce() {
        return energy > energyReadyToReproduce;  }

    public final List<Integer> getGenome() {
        return new ArrayList<>(this.genome);
    }
    public int getEnergy(){
        return this.energy;
    }
    public int getChildrenCount(){
        return this.childrenCount;
    }

    public int getPlantsEaten(){
        return this.plantsEaten;
    }

    public int getAge(){
        return this.daysLived;
    }
    @Override
    public int hashCode() {
        return (31 * getEnergy() * getAge());
    }
}

