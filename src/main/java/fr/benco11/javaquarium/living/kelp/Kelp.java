package fr.benco11.javaquarium.living.kelp;

import fr.benco11.javaquarium.living.Living;

import java.util.Optional;

/**
 * Algue
 */
public sealed interface Kelp extends Living permits KelpBasic {
    /**
     * Essaye de se reproduire (division)
     *
     * @return un <code>Optional</code> contenant ou non une nouvelle algue
     */
    Optional<Kelp> reproduce();
}
