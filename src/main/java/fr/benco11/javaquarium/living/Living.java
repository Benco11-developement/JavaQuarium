package fr.benco11.javaquarium.living;

import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;

public sealed interface Living permits Fish, Kelp {
    boolean tick();

    boolean alive();

    void bitten();
}
