package fr.benco11.javaquarium;

import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.options.Options;
import fr.benco11.javaquarium.utils.Pair;

import java.util.List;
import java.util.Map;

public interface Aquarium {
    /**
     * Ajoute un être vivant à l'aquarium
     *
     * @param living être vivant (poisson ou algue)
     */
    void add(Living living);

    /**
     * Supprime un être vivant s'il est présent dans l'aquarium
     *
     * @param living être vivant (poisson ou algue)
     */
    void remove(Living living);

    /**
     * Avance et simule d'un tour l'aquarium puis met à jour la liste des poissons et algues
     */
    void update();

    /**
     * Recense et affiche la liste des êtres vivants
     */
    void census();

    /**
     * Donne la liste des êtres vivants qui n'ont pas encore été ajoutés
     *
     * @return une liste d'êtres vivants à ajouter par tour
     */
    Map<Integer, List<Living>> remainingLivingsToAdd();

    /**
     * Donne la liste des filtres de suppression d'êtres vivants qui n'ont pas encore été appliqués
     *
     * @return une liste de filtres pour les algues et une autre pour les poissons par tour
     */
    Map<Integer, Pair<List<Options>, List<Options>>> remainingRemoveOptions();

    /**
     * Donne la liste des algues vivantes
     *
     * @return liste des algues
     */
    List<Kelp> kelps();

    /**
     * Donne la liste des poissons vivants
     *
     * @return liste des poissons
     */
    List<Fish> fishes();
}
