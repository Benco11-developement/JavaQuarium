package fr.benco11.javaquarium.living;

import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;

/**
 * Être vivant
 */
public sealed interface Living permits Fish, Kelp {
    /**
     * Simule une fin de tour pour l'être vivant
     *
     * @return si l'être vivant est encore en vie
     */
    boolean tick();

    /**
     * Renvoie vrai si l'être vivant est encore en vie
     */
    boolean alive();

    /**
     * Simule que l'être vivant se fasse mordre
     */
    void bitten();

    /**
     * Renvoie l'âge de l'être vivant
     */
    int age();

    /**
     * Renvoie le nombre de pvs de l'être vivant
     */
    int pv();
}
