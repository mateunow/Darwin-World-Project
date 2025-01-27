package darwinProject.model.util;

import darwinProject.model.Animal;

import java.util.*;

public class AnimalPriorityComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal a1, Animal a2) {

        int energyComparison = Integer.compare(a2.getEnergy(), a1.getEnergy());
        if (energyComparison != 0) {
            return energyComparison;
        }

        int ageComparison = Integer.compare(a2.getAge(), a1.getAge());
        if (ageComparison != 0) {
            return ageComparison;
        }
        int childrenComparison = Integer.compare(a2.getChildrenCount(), a1.getChildrenCount());
        if (childrenComparison != 0) {
            return childrenComparison;
        }
        return 0;
    }
}
