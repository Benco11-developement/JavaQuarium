package fr.benco11.javaquarium.living;

import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;

/**
 * Être pouvant manger
 *
 * @param <T> type de nourriture
 */
public interface Eater<T extends Living> {
    /**
     * Simule que l'être mange de la nourriture
     *
     * @param food nourriture
     */
    void eat(T food);

    interface Carnivorous extends Eater<Fish> {
    }

    interface Herbivorous extends Eater<Kelp> {
    }
}
