package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.StringUtils;

import java.util.Optional;

/**
 * Carp (carpe)
 */
public final class CarpFish extends Fish.HerbivorousFish {
    public CarpFish(String name, Sex sex) {
        super(name, sex);
    }

    public CarpFish(String name, Sex sex, int age, int pv) {
        super(name, sex, age, pv);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof CarpFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new CarpFish(StringUtils.getFishName(this), Fish.Sex.randomSex()));
    }
}
