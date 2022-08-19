package fr.benco11.javaquarium.living;

import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;

public interface Eater<T extends Living> {
    void eat(T food);

    interface Carnivorous extends Eater<Fish> {
    }

    interface Herbivorous extends Eater<Kelp> {
    }
}
