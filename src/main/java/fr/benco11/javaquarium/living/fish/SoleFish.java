package fr.benco11.javaquarium.living.fish;

import java.util.Optional;

/**
 * Sole (sole)
 */
public final class SoleFish extends Fish.HerbivorousFish {
    public SoleFish(String name, Sex sex) {
        super(name, sex);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof SoleFish) || other == this) return Optional.empty();
        sex = Fish.Sex.opposite(other.sex());
        return Optional.of(new SoleFish("", Fish.Sex.randomSex()));
    }
}
