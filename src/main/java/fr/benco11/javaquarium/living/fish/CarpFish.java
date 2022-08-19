package fr.benco11.javaquarium.living.fish;

import java.util.Optional;

/**
 * Carp (carpe)
 */
public final class CarpFish extends Fish.HerbivorousFish {
    public CarpFish(String name, Sex sex) {
        super(name, sex);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof CarpFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new CarpFish("", Fish.Sex.randomSex()));
    }
}
