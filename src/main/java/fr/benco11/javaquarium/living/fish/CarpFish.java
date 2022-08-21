package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.NameUtils;

import java.util.Optional;

/**
 * Carp (carpe)
 */
public final class CarpFish extends Fish.HerbivorousFish {
    public CarpFish(String name, Sex sex) {
        super(name, sex);
    }

    public CarpFish(String name, Sex sex, int age) {
        super(name, sex, age);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof CarpFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new CarpFish(NameUtils.getName(this), Fish.Sex.randomSex()));
    }
}
