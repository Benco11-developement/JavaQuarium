package fr.benco11.javaquarium.living.kelp;

import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.kelp.KelpBasic;

import java.util.Optional;

public sealed interface Kelp extends Living permits KelpBasic {
    Optional<Kelp> reproduce();
}
