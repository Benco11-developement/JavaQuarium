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
     * Renvoie vrai si l'être est encore en vie
     *
     * @return si l'être est vivant
     */
    boolean alive();

    /**
     * Simule que l'être vivant se fasse mordre
     */
    void bitten();

    /**
     * Renvoie l'âge de l'être vivant
     *
     * @return l'âge de l'être
     */
    int age();

    /**
     * Renvoie le nombre de pvs de l'être vivant
     *
     * @return le nombre de pvs
     */
    int pv();
}
