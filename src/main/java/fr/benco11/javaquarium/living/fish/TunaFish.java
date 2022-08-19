package fr.benco11.javaquarium.living.fish;

import java.util.Optional;

/**
 * Tuna (thon)
 */
public final class TunaFish extends Fish.CarnivorousFish {
    public TunaFish(String name, Sex sex) {
        super(name, sex);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof TunaFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new TunaFish("", Fish.Sex.randomSex()));
    }
}
